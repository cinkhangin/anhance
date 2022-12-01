@file:Suppress("unused")

package com.naulian.anhance

import android.content.Context
import android.service.quicksettings.Tile
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun Fragment.showLoadingDialog(message: String = "Please wait") {
    AnLoadingDialog.show(requireContext(), message)
}

fun Fragment.dismissLoadingDialog() {
    AnLoadingDialog.dismiss()
}

fun Fragment.updateLoadingMessage(message: String){
    AnLoadingDialog.updateMessage(message)
}

fun Fragment.updateLoadingTitle(title: String){
    AnLoadingDialog.updateTitle(title)
}

object AnLoadingDialog {
    private var dialog: AlertDialog? = null

    fun show(context: Context, title: String) {
        val progress = ProgressBar(context)
        dialog = MaterialAlertDialogBuilder(context)
            .setView(progress)
            .setTitle(title)
            .setMessage(" ")
            .setCancelable(false)
            .setPositiveButton(" ") { _, _ ->
            }
            .create()

        dialog?.show()
    }

    fun updateMessage(message: String) {
        dialog?.setMessage(message)
    }

    fun updateTitle(title: String) {
        dialog?.setTitle(title)
    }

    fun dismiss() {
        dialog?.dismiss()
    }

}