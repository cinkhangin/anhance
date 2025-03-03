@file:Suppress("unused")

package com.naulian.anhance

import android.content.Context
import android.widget.Toast

fun showTextToast(context: Context, message: String?) {
    Toast.makeText(context, "$message", Toast.LENGTH_SHORT).show()
}

fun Context.showToast(message: String?) {
    showTextToast(this, message)
}

fun toastError(context: Context, throwable: Throwable?) {
    context.showToast(throwable?.message ?: "Error")
}
