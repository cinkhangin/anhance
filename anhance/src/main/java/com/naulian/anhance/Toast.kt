@file:Suppress("unused")

package com.naulian.anhance

import android.content.Context
import android.widget.Toast
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

fun showTextToast(context: Context, message: String?) {
    Toast.makeText(context, "$message", Toast.LENGTH_SHORT).show()
}

fun Context.showToast(message: String?) {
    showTextToast(this, message)
}

fun showToastError(context: Context, throwable: Throwable?) {
    context.showToast(throwable?.message ?: "Error")
}

fun Context.toastError(throwable: Throwable?) {
    showToastError(this, throwable)
}

data class ToastMessage(
    val text: String = "",
    val isError: Boolean = false
) {
    companion object {
        val Empty = ToastMessage()
    }
}

object FlowToast {
    const val LENGTH_SHORT = 2500L
    const val LENGTH_LONG = 4000L

    private val _message = MutableStateFlow(ToastMessage.Empty)
    val message = _message.asStateFlow()

    suspend fun show(text: String, duration: Long = LENGTH_SHORT, isError: Boolean = false) {
        _message.update { ToastMessage(text = text, isError = isError) }
        delay(duration)
        _message.update { ToastMessage.Empty }
    }

    suspend fun showErr(text: String, duration: Long = LENGTH_SHORT) {
        show(text = text, duration = duration, isError = true)
    }
}


