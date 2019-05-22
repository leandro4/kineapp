package com.gon.kineapp.utils

import android.content.Context
import android.support.v7.app.AlertDialog
import android.view.Window
import com.gon.kineapp.R

object DialogUtil {

    fun showGenericAlertDialog(context: Context, title: String, message: String) {
        val dialog = AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(context.resources.getString(R.string.generic_accept_button)) { dialog, _ -> dialog.dismiss() }.create()
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.show()
    }

}