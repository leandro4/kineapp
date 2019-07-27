package com.gon.kineapp.ui.activities

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.SurfaceTexture
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.TextureView
import android.widget.Toast
import com.gon.kineapp.R
import com.gon.kineapp.utils.DialogUtil
import com.gon.kineapp.utils.Utils

abstract class BaseCameraActivity : AppCompatActivity(), TextureView.SurfaceTextureListener {

    private val CAMERA_PERMISSION_CODE = 10001

    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {}

    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean { return false }

    override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {}

    protected fun isPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    abstract fun onPermissionGranted()

    protected fun askForPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            DialogUtil.showGenericAlertDialogConfirm(this, getString(R.string.camera_ask_permission_title),
                getString(R.string.camera_ask_permission_msg)) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE)
            }
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {

        when (requestCode) {
            CAMERA_PERMISSION_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    onPermissionGranted()
                } else {
                    Toast.makeText(this, getString(R.string.camera_permission_denied), Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }
}