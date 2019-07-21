package com.gon.kineapp.ui.activities

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.SurfaceTexture
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.TextureView
import android.widget.Toast
import com.gon.kineapp.R
import com.gon.kineapp.utils.DialogUtil
import com.gon.kineapp.utils.Utils

abstract class BaseCameraActivity : AppCompatActivity(), TextureView.SurfaceTextureListener {

    protected var permissionOk = false
    private val CAMERA_PERMISSION_CODE = 10001

    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {}

    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean { return false }

    override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {}

    override fun onResume() {
        super.onResume()
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            askForPermission()
        } else {
            permissionOk = true
        }
    }

    private fun askForPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            DialogUtil.showGenericAlertDialog(this, getString(R.string.camera_ask_permission_title), getString(R.string.camera_ask_permission_msg))
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS), CAMERA_PERMISSION_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {

        when (requestCode) {
            CAMERA_PERMISSION_CODE -> {
                permissionOk = if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    true
                } else {
                    Toast.makeText(this, getString(R.string.camera_permission_denied), Toast.LENGTH_SHORT).show()
                    false
                }
                return
            }
        }
    }
}