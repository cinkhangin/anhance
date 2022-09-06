package com.naulian.anhance

import android.content.Context
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder

object LoadingDialog {
    private var dialog: AlertDialog? = null

    fun show(context: Context, message : String = "Please wait...") {
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

    fun dismiss() {
        dialog?.dismiss()
    }

}