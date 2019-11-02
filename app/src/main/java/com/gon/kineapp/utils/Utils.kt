package com.gon.kineapp.utils

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import android.provider.MediaStore
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.net.Uri
import android.os.Environment
import androidx.core.app.ActivityCompat
import java.io.File
import com.gon.kineapp.R
import com.vincent.videocompressor.VideoCompress

object Utils {

    private val PERMISSIONS = arrayOf(
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
        android.Manifest.permission.RECORD_AUDIO,
        android.Manifest.permission.CAMERA
    )

    fun showKeyboard(view: EditText) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    fun hideKeyboardFrom(view: View) {
        val imm = view.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun convertImage(base64Str: String): Bitmap {
        val decodedBytes = Base64.decode(base64Str.substring(base64Str.indexOf(",") + 1), Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    }

    fun convertImage(bitmap: Bitmap): String {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)

        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
    }

    fun formatDate(date: String): String {
        return try {
            var spf = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS")
            val newDate = spf.parse(date)
            spf = SimpleDateFormat("dd MMM yyyy")
            spf.format(newDate)
        } catch (e: Exception) {
            "Check date"
        }
    }

    fun takeVideo(activity: Activity, requestCode: Int) {
        DialogUtil.showOptionsAlertDialog(activity, activity.getString(R.string.take_video_title), activity.getString(R.string.take_video_msg)) {
            if (hasVideoRecordPermissions(activity, *PERMISSIONS)) {
                recordVideo(activity, requestCode)
            } else {
                DialogUtil.showOptionsAlertDialog(activity, activity.getString(R.string.request_permissions_title), activity.getString(R.string.request_permissions_message)) {
                    askForRecordPermissions(activity)
                }
            }
        }
    }

    private fun recordVideo(activity: Activity, requestCode: Int) {
        //val timestamp = System.currentTimeMillis()
        //val mediaFile = File(getExternalStorageDirectory().absolutePath + "/kine_" + timestamp + ".mp4")
        val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)

        /*val videoUri = FileProvider.getUriForFile(activity, "com.gon.kineapp.provider", mediaFile)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri)*/

        //intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0)
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 20)

        intent.resolveActivity(activity.packageManager)?.also {
            activity.startActivityForResult(intent, requestCode)
        }
    }

    private fun askForRecordPermissions(activity: Activity) {
        if (!hasVideoRecordPermissions(activity, *PERMISSIONS)) {
            ActivityCompat.requestPermissions(activity, PERMISSIONS, Constants.PERMISSIONS_REQUEST_CODE)
        }
    }

    private fun hasVideoRecordPermissions(context: Context, vararg permissions: String): Boolean = permissions.all {
        ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }

    fun compressVideo(uri: Uri, contentResolver: ContentResolver, onError: (error: String) -> Unit, onCompress: (uriOutput: String) -> Unit) {
        val cursor = contentResolver.query(uri,null,null,null,null)
        if (cursor == null) {
            onError("Error al grabar video")
            return
        }

        cursor.moveToFirst()
        val videoPath = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA))
        cursor.close()

        val folderFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath
        val outFile = folderFile + File.separator + "kineapp_" + System.currentTimeMillis() + ".mp4"

        VideoCompress.compressVideoLow(videoPath, outFile, object : VideoCompress.CompressListener {
            override fun onStart() {}

            override fun onSuccess() {
                onCompress(outFile)
            }

            override fun onFail() {
                onError(Resources.getSystem().getString(R.string.error_comprees_video))
            }

            override fun onProgress(percent: Float) {

            }
        })
    }
}