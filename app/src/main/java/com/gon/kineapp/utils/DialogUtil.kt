package com.gon.kineapp.utils

import android.content.Context
import androidx.appcompat.app.AlertDialog
import android.view.Window
import com.gon.kineapp.R

object DialogUtil {

    fun showGenericAlertDialog(context: Context, title: String, message: String) {
        showGenericAlertDialogConfirm(context, title, message) {}
    }

    fun showGenericAlertDialogConfirm(context: Context, title: String, message: String, confirmAction: () -> Unit) {
        val dialog = AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(context.resources.getString(R.string.generic_accept_button)) { dialog, _ ->
                run {
                    dialog.dismiss()
                    confirmAction()
                }
            }.create()
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.show()
    }

    fun showOptionsAlertDialog(context: Context, title: String, message: String, positiveAction: () -> Unit) {
        val dialog = AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(context.resources.getString(R.string.generic_accept_button)) { dialog, _ ->
                run {
                    dialog.dismiss()
                    positiveAction()
                }
            }
            .setNegativeButton(context.resources.getString(R.string.generic_cancel)) { dialog, _ -> dialog.dismiss() }.create()
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.show()
    }

    fun showChooserListDialog(context: Context, title: String, options: List<String>, listener: (which: Int) -> Unit) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setItems(options.toTypedArray()) { _, which -> listener(which) }
        builder.show()
    }
}