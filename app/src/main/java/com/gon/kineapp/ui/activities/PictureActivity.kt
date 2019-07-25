package com.gon.kineapp.ui.activities

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.ImageFormat
import android.graphics.Point
import android.graphics.SurfaceTexture
import android.hardware.camera2.*
import android.hardware.camera2.params.StreamConfigurationMap
import android.media.Image
import android.media.ImageReader
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.util.Size
import android.util.SparseIntArray
import android.view.Display
import android.view.Surface
import android.view.TextureView
import android.widget.ImageView
import android.widget.Toast
import com.gon.kineapp.R
import com.gon.kineapp.utils.StateCameraCallback
import kotlinx.android.synthetic.main.activity_picture.*
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*

class PictureActivity : BaseCameraActivity(), ImageReader.OnImageAvailableListener {

    private var previewsize: Size? = null
    private var jpegSizes: Array<Size>? = null
    private var textureView: TextureView? = null
    private var cameraDevice: CameraDevice? = null
    private var previewBuilder: CaptureRequest.Builder? = null
    private var previewSession: CameraCaptureSession? = null
    private var getPicture: ImageView? = null

    private val stateCallback = object : StateCameraCallback() {
        override fun onOpened(camera: CameraDevice) {
            cameraDevice = camera
            startCamera()
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picture)
        textureView = findViewById(R.id.textureView)
        getPicture = findViewById(R.id.btnTakePicture)
        textureView!!.surfaceTextureListener = this
        getPicture!!.setOnClickListener { v -> getPicture() }
    }

    private fun getPicture() {
        if (cameraDevice != null) {
            val manager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
            try {
                val characteristics = manager.getCameraCharacteristics(cameraDevice!!.id)
                if (characteristics != null) {
                    jpegSizes =
                        characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)!!.getOutputSizes(ImageFormat.JPEG)
                }
                var width = 640
                var height = 480
                if (jpegSizes != null && jpegSizes!!.size > 0) {
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
                val rotation = windowManager.defaultDisplay.rotation
                capturebuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(rotation))
                val handlerThread = HandlerThread("takepicture")
                handlerThread.start()
                val handler = Handler(handlerThread.looper)
                reader.setOnImageAvailableListener(this, handler)
                val previewSSession = object : CameraCaptureSession.CaptureCallback() {
                    override fun onCaptureStarted(
                        session: CameraCaptureSession,
                        request: CaptureRequest,
                        timestamp: Long,
                        frameNumber: Long
                    ) {
                        super.onCaptureStarted(session, request, timestamp, frameNumber)
                    }

                    override fun onCaptureCompleted(
                        session: CameraCaptureSession,
                        request: CaptureRequest,
                        result: TotalCaptureResult
                    ) {
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
        var image: Image? = null
        try {
            image = reader?.acquireLatestImage()
            val buffer = image!!.planes[0].buffer
            val bytes = ByteArray(buffer.capacity())
            buffer.get(bytes)
            save(bytes)
            image.close()
        } catch (ee: Exception) {
            ee.printStackTrace()
        }
    }

    private fun save(bytes: ByteArray) {
        Toast.makeText(this, "foto: "+bytes.size, Toast.LENGTH_SHORT).show()
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
            val w = getWitdthScreen()
            val h = getHeightScreen()
            previewsize = map!!.getOutputSizes(SurfaceTexture::class.java)[5]
            if (isPermissionGranted()) {
                manager.openCamera(camerId, stateCallback, null)
            } else {
                askForPermission()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun getWitdthScreen(): Int {
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        return size.x
    }

    private fun getHeightScreen(): Int {
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        return size.y
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