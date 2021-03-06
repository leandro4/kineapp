package com.gon.kineapp.ui.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.hardware.camera2.*
import android.media.Image
import android.media.ImageReader
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Size
import android.util.SparseIntArray
import android.view.Surface
import android.view.TextureView
import android.view.View
import com.gon.kineapp.R
import com.gon.kineapp.utils.Constants
import com.gon.kineapp.utils.StateCameraCallback
import com.gonanimationlib.animations.Animate
import kotlinx.android.synthetic.main.save_cancel_picture.*
import java.util.*
import android.graphics.Bitmap
import com.gon.kineapp.utils.Utils
import kotlinx.android.synthetic.main.activity_picture.*
import java.io.ByteArrayOutputStream
import android.util.Base64
import android.widget.*
import com.gon.kineapp.model.PhotoTag
import com.gonanimationlib.animations.CompSlide
import kotlinx.android.synthetic.main.view_loading.*

class PictureActivity : BaseCameraActivity(), ImageReader.OnImageAvailableListener, AdapterView.OnItemSelectedListener {

    private var previewsize: Size? = null
    private var jpegSizes: Array<Size>? = null
    private var textureView: TextureView? = null
    private var cameraDevice: CameraDevice? = null
    private var previewBuilder: CaptureRequest.Builder? = null
    private var previewSession: CameraCaptureSession? = null
    private var getPicture: ImageView? = null
    private var bitmap: Bitmap? = null
    private var encodedPhoto: String? = null
    private var tag = PhotoTag.O

    private val stateCallback = object : StateCameraCallback() {
        override fun onOpened(camera: CameraDevice) {
            cameraDevice = camera
            startCamera()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picture)

        textureView = findViewById(R.id.textureView)
        getPicture = findViewById(R.id.btnTakePicture)
        textureView!!.surfaceTextureListener = this
        loadingView.visibility = View.GONE
        getPicture!!.setOnClickListener {
            loadingView.visibility = View.VISIBLE
            rlPreview.visibility = View.VISIBLE
            Animate.ALPHA(1f).duration(Animate.DURATION_MEDIUM).startAnimation(rlPreview)
            getPicture()
        }
        initUI()
    }

    private fun initUI() {
        btnReject.setOnClickListener {
            llMedia.visibility = View.GONE
            Animate.ALPHA(0f).duration(Animate.DURATION_MEDIUM).onEnd { rlPreview.visibility = View.GONE }.startAnimation(rlPreview)
            preview.setImageBitmap(null)
        }
        btnAccept.setOnClickListener {
            val intent = Intent()
            intent.putExtra(Constants.PHOTO_EXTRA, encodedPhoto)
            intent.putExtra(Constants.PHOTO_TAG_EXTRA, tag.name)
            setResult(Activity.RESULT_OK, intent)
            this@PictureActivity.finish()
        }
        tvHide.setOnClickListener {
            ivArrow.visibility = View.VISIBLE
            Animate.SLIDE(CompSlide.TO.LEFT).relative(CompSlide.RELATIVE_TO.PARENT).duration(Animate.DURATION_SHORT).onEnd { llTemplate.visibility = View.GONE }.startAnimation(llTemplate)
            Animate.SLIDE(CompSlide.FROM.LEFT).relative(CompSlide.RELATIVE_TO.PARENT).duration(Animate.DURATION_SHORT).startAnimation(ivArrow)
        }
        ivArrow.setOnClickListener {
            llTemplate.visibility = View.VISIBLE
            Animate.SLIDE(CompSlide.FROM.LEFT).relative(CompSlide.RELATIVE_TO.PARENT).duration(Animate.DURATION_SHORT).startAnimation(llTemplate)
            Animate.SLIDE(CompSlide.TO.LEFT).relative(CompSlide.RELATIVE_TO.PARENT).duration(Animate.DURATION_SHORT).onEnd { ivArrow.visibility = View.GONE }.startAnimation(ivArrow)
        }

        val adapter = ArrayAdapter.createFromResource(this, R.array.pics_kind_array, R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(R.layout.spinner_text_arrow_white)
        spKind.adapter = adapter
        spKind.onItemSelectedListener = this

        sbWidth.max = 3
        sbWidth.incrementProgressBy(1)
        sbHeight.max = 2
        sbHeight.incrementProgressBy(1)

        sbWidth.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                ivSiluet.setImageResource(tag.getDrawable(progress, sbHeight.progress))
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
        sbHeight.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                ivSiluet.setImageResource(tag.getDrawable(sbWidth.progress, progress))
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (position) {
            0 -> {
                tag = PhotoTag.F
            }
            1 -> {
                tag = PhotoTag.D
            }
            2 -> {
                tag = PhotoTag.E
            }
            3 -> {
                tag = PhotoTag.I
            }
            4 -> {
                tag = PhotoTag.O
                setVisibleControls(false)
                return
            }
        }
        ivSiluet.setImageResource(tag.getDrawable(sbWidth.progress, sbHeight.progress))
        setVisibleControls(true)
    }

    private fun setVisibleControls(visible: Boolean) {
        if (visible) {
            ivSiluet.visibility = View.VISIBLE
            if (ivArrow.visibility == View.GONE && llTemplate.visibility == View.GONE) {
                ivArrow.visibility = View.VISIBLE
                Animate.SLIDE(CompSlide.FROM.LEFT).relative(CompSlide.RELATIVE_TO.PARENT).duration(Animate.DURATION_SHORT).startAnimation(ivArrow)
            }
        } else {
            ivSiluet.visibility = View.GONE
            if (llTemplate.visibility == View.VISIBLE) {
                Animate.SLIDE(CompSlide.TO.LEFT).relative(CompSlide.RELATIVE_TO.PARENT).duration(Animate.DURATION_SHORT).onEnd { llTemplate.visibility = View.GONE }.startAnimation(llTemplate)
            }
            if (ivArrow.visibility == View.VISIBLE) {
                Animate.SLIDE(CompSlide.TO.LEFT).relative(CompSlide.RELATIVE_TO.PARENT).duration(Animate.DURATION_SHORT).onEnd { ivArrow.visibility = View.GONE }.startAnimation(ivArrow)
            }
        }
    }

    internal fun startCamera() {
        if (cameraDevice == null || !textureView!!.isAvailable || previewsize == null) {
            return
        }

        val texture = textureView!!.surfaceTexture ?: return

        texture.setDefaultBufferSize(previewsize!!.width, previewsize!!.height)
        val surface = Surface(texture)
        try {
            previewBuilder = cameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        previewBuilder!!.addTarget(surface)
        try {
            cameraDevice!!.createCaptureSession(Arrays.asList(surface), object : CameraCaptureSession.StateCallback() {
                override fun onConfigured(session: CameraCaptureSession) {
                    previewSession = session
                    getChangedPreview()
                }

                override fun onConfigureFailed(session: CameraCaptureSession) {}
            }, null)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    internal fun getChangedPreview() {
        if (cameraDevice != null) {
            previewBuilder!!.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO)
            val thread = HandlerThread("changed Preview")
            thread.start()
            val handler = Handler(thread.looper)
            try {
                previewSession!!.setRepeatingRequest(previewBuilder!!.build(), null, handler)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    private fun getPicture() {
        if (cameraDevice != null) {
            val manager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
            try {
                val characteristics = manager.getCameraCharacteristics(cameraDevice!!.id)

                jpegSizes = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)!!.getOutputSizes(ImageFormat.JPEG)

                var width = 640
                var height = 480
                if (jpegSizes != null && jpegSizes!!.isNotEmpty()) {
                    width = jpegSizes!![0].width
                    height = jpegSizes!![0].height
                }
                val reader = ImageReader.newInstance(width, height, ImageFormat.JPEG, 1)
                val outputSurfaces = ArrayList<Surface>(2)
                outputSurfaces.add(reader.surface)
                outputSurfaces.add(Surface(textureView!!.surfaceTexture))
                val capturebuilder = cameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)
                capturebuilder.addTarget(reader.surface)
                capturebuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO)
                //val rotation = windowManager.defaultDisplay.rotation
                capturebuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(1))
                val handlerThread = HandlerThread("takepicture")
                handlerThread.start()
                val handler = Handler(handlerThread.looper)
                reader.setOnImageAvailableListener(this, handler)
                val previewSSession = object : CameraCaptureSession.CaptureCallback() {
                    override fun onCaptureCompleted(session: CameraCaptureSession, request: CaptureRequest, result: TotalCaptureResult) {
                        super.onCaptureCompleted(session, request, result)
                        startCamera()
                    }
                }
                cameraDevice!!.createCaptureSession(outputSurfaces, object : CameraCaptureSession.StateCallback() {
                    override fun onConfigured(session: CameraCaptureSession) {
                        try {
                            session.capture(capturebuilder.build(), previewSSession, handler)
                        } catch (e: Exception) {
                        }

                    }

                    override fun onConfigureFailed(session: CameraCaptureSession) {}
                }, handler)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onImageAvailable(reader: ImageReader?) {
        try {
            val image = reader?.acquireLatestImage()
            val buffer = image!!.planes[0].buffer
            val bytes = ByteArray(buffer.capacity())
            buffer.get(bytes)
            save(bytes)
            image.close()
        } catch (ee: Exception) {
            ee.printStackTrace()
        }
    }

    @SuppressLint("CheckResult")
    private fun save(bytes: ByteArray) {
        bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

        if (bitmap!!.width > bitmap!!.height) {
            val matrix = Matrix()
            matrix.preRotate(90f)
            bitmap = Bitmap.createBitmap(bitmap!!, 0, 0, bitmap!!.width, bitmap!!.height, matrix, true)
        }
        while (bitmap?.byteCount!!.compareTo(12100100) == 1) {
            bitmap = Bitmap.createScaledBitmap(bitmap!!, bitmap!!.width*3/4, bitmap!!.height*3/4, false)
        }
        val outArray = ByteArrayOutputStream()
        bitmap!!.compress(Bitmap.CompressFormat.JPEG, 30, outArray)
        encodedPhoto = Base64.encodeToString(outArray.toByteArray(), Base64.DEFAULT)

        val mainHandler = Handler(this.mainLooper)
        val myRunnable = Runnable {
            preview.setImageBitmap(bitmap)
            llMedia.visibility = View.VISIBLE
            loadingView.visibility = View.GONE
        }
        mainHandler.post(myRunnable)
    }

    override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
        openCamera()
    }

    override fun onPause() {
        super.onPause()
        if (cameraDevice != null) {
            cameraDevice!!.close()
        }
    }

    @SuppressLint("MissingPermission")
    private fun openCamera() {
        val manager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        try {
            val camerId = manager.cameraIdList[0]
            val characteristics = manager.getCameraCharacteristics(camerId)
            val map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)

            map!!.getOutputSizes(SurfaceTexture::class.java).forEach {
                if (it.width.toFloat()/it.height.toFloat() == 16.0f/9.0f) {
                    previewsize = it
                    return@forEach
                }
            }

            if (isPermissionGranted()) {
                manager.openCamera(camerId, stateCallback, null)
            } else {
                askForPermission()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onPermissionGranted() {
        openCamera()
    }

    companion object {
        private val ORIENTATIONS = SparseIntArray()
        init {
            ORIENTATIONS.append(Surface.ROTATION_0, 90)
            ORIENTATIONS.append(Surface.ROTATION_90, 0)
            ORIENTATIONS.append(Surface.ROTATION_180, 270)
            ORIENTATIONS.append(Surface.ROTATION_270, 180)
        }
    }
}
