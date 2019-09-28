package com.gon.kineapp.utils

import android.app.Activity
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
import android.net.Uri
import android.os.Environment.getExternalStorageDirectory
import java.io.File
import com.gon.kineapp.ui.activities.MainActivity
import android.support.v4.content.FileProvider
import com.gon.kineapp.R


object Utils {

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
            val timestamp = System.currentTimeMillis()
            val mediaFile = File(getExternalStorageDirectory().absolutePath + "/kine_" + timestamp + ".mp4")
            val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)

            /*val videoUri = FileProvider.getUriForFile(activity, "com.gon.kineapp.provider", mediaFile)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri)*/

            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0)
            intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 20)

            intent.resolveActivity(activity.packageManager)?.also {
                activity.startActivityForResult(intent, requestCode)
            }
        }
    }
}