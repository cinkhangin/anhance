@file:Suppress("unused")

package com.naulian.anhance

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.fragment.app.Fragment

fun Activity.showToast(message: String?) =
    showTextToast(this, message)

fun Fragment.showToast(message: String?) {
    showTextToast(requireContext(), message)
}

fun showTextToast(context: Context, message: String?) {
    Toast.makeText(context, "$message", Toast.LENGTH_SHORT).show()
}

fun Context.showToast(message: String?) {
    showTextToast(this, message)
}

fun toastError(context: Context, throwable: Throwable?) {
    context.showToast(throwable?.message ?: "Error")
}
