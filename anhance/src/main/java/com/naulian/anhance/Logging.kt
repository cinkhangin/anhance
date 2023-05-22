@file:Suppress("unused")

package com.naulian.anhance

import android.util.Log

@Deprecated(
    message = "Use logInfo(TAG, message) instead for better logging. Deprecated since 0.3.0",
    replaceWith = ReplaceWith("logInfo(TAG, message)"),
)
fun <T> logInfo(value: T) {
    Log.i("logInfo", "$value")
}

fun <T> logInfo(tag: String, message: T) {
    Log.i(tag, "$message")
}

@Deprecated(
    message = "Use logDebug(TAG, message) instead for better logging. Deprecated since 0.3.0",
    replaceWith = ReplaceWith("logDebug(TAG, message)")
)
fun <T> logDebug(value: T) {
    Log.d("logDebug", "$value")
}

fun <T> logDebug(tag: String, message: T) {
    Log.d(tag, "$message")
}

@Deprecated(
    message = "Use logError(TAG, message) instead for better logging. Deprecated since 0.3.0",
    replaceWith = ReplaceWith("logError(TAG, message)")
)
fun <T> logError(value: T) {
    Log.e("logError", "$value")
}

fun <T> logError(tag: String, message: T) {
    Log.e(tag, "$message")
}