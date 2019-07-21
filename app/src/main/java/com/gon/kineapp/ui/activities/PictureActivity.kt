package com.gon.kineapp.ui.activities

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.ImageFormat
import android.graphics.SurfaceTexture
import android.hardware.camera2.*
import android.hardware.camera2.params.StreamConfigurationMap
import android.media.Image
import android.media.ImageReader
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Size
import android.util.SparseIntArray
import android.view.Surface
import android.widget.Toast
import com.gon.kineapp.R
import com.gon.kineapp.utils.StateCameraCallback
import kotlinx.android.synthetic.main.activity_picture.*
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.*

class PictureActivity : BaseCameraActivity(), ImageReader.OnImageAvailableListener {

    private var previewsize: Size? = null
    private var jpegSizes: Array<Size>? = null
    private var cameraDevice: CameraDevice? = null
    private var previewBuilder: CaptureRequest.Builder? = null
    private var previewSession: CameraCaptureSession? = null
    private val ORIENTATIONS = SparseIntArray()

    private val stateCallback = object : StateCameraCallback() {
        override fun onOpened(camera: CameraDevice) {
            cameraDevice = camera
            startCamera()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picture)
        initOrientations()
        textureView.surfaceTextureListener = this
        btnTakePicture.setOnClickListener{ getPicture() }
    }

    private fun initOrientations() {
        ORIENTATIONS.append(Surface.ROTATION_0, 90)
        ORIENTATIONS.append(Surface.ROTATION_90, 0)
        ORIENTATIONS.append(Surface.ROTATION_180, 270)
        ORIENTATIONS.append(Surface.ROTATION_270, 180)
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
                outputSurfaces.add(Surface(textureView.surfaceTexture))
                val captureBuilder = cameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)
                captureBuilder.addTarget(reader.surface)
                captureBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO)
                val rotation = windowManager.defaultDisplay.rotation
                captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(rotation))

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
                            session.capture(captureBuilder.build(), previewSSession, handler)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                    }

                    override fun onConfigureFailed(session: CameraCaptureSession) {}

                }, handler)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    private fun startCamera() {
        if (cameraDevice != null && textureView.isAvailable && previewsize != null) {

            val texture = textureView.surfaceTexture ?: return

            texture.setDefaultBufferSize(previewsize!!.width, previewsize!!.height)
            val surface = Surface(texture)

            cameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW).addTarget(surface)
            cameraDevice!!.createCaptureSession(Arrays.asList(surface), object : CameraCaptureSession.StateCallback() {

                    override fun onConfigured(session: CameraCaptureSession) {
                        previewSession = session
                        getChangedPreview()
                    }

                    override fun onConfigureFailed(session: CameraCaptureSession) {}

                },null)
        }
    }

    private fun getChangedPreview() {
        if (cameraDevice != null) {
            previewBuilder?.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO)
            val thread = HandlerThread("changed Preview")
            thread.start()
            val handler = Handler(thread.looper)
            try {
                previewSession?.setRepeatingRequest(previewBuilder!!.build(), null, handler)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onSurfaceTextureAvailable(surface: SurfaceTexture?, width: Int, height: Int) {
        openCamera()
    }

    override fun onImageAvailable(reader: ImageReader?) {
        var image: Image?
        reader?.let {
            image = it.acquireLatestImage()
            val buffer = image!!.planes[0].buffer
            val bytes = ByteArray(buffer.capacity())
            buffer.get(bytes)
            onPictureReady(bytes)
            image?.close()
        }
    }

    private fun onPictureReady(bytes: ByteArray) {
        Toast.makeText(this, "foto lista", Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("MissingPermission") // el permiso se pide en la clase padre
    private fun openCamera() {
        val manager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        try {
            val cameraId = manager.cameraIdList[0]
            val characteristics = manager.getCameraCharacteristics(cameraId)
            val map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
            previewsize = map!!.getOutputSizes(SurfaceTexture::class.java)[0]
            if (permissionOk) {
                manager.openCamera(cameraId, stateCallback, null)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onPause() {
        super.onPause()
        cameraDevice?.close()
    }
}