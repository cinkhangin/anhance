package com.naulian.anhance

import android.content.Context
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder

object AnLoadingDialog {
    private var dialog: AlertDialog? = null

    fun show(context: Context, message : String) {
        val progress = ProgressBar(context)
        dialog = MaterialAlertDialogBuilder(context)
            .setView(progress)
            .setTitle(message)
            .setMessage(" ")
            .setCancelable(false)
            .setPositiveButton(" ") { _, _ ->
            }
            .create()

        dialog?.show()
    }

    fun updateMessage(message: String){
        dialog?.setMessage(message)
    }

    fun dismiss() {
        dialog?.dismiss()
    }

}