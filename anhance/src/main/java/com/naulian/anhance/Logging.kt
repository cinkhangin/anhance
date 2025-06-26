@file:Suppress("unused")

package com.naulian.anhance

import android.util.Log


@Deprecated(message = "Deprecated")
fun <T> logInfo(value: T) = Log.i("Info", "$value")
@Deprecated(message = "Deprecated")
fun <T> logInfo(tag: String, message: T) = Log.i(tag, "$message")

@Deprecated(message = "Deprecated")
fun <T> logDebug(value: T) = Log.d("Debug", "$value")
@Deprecated(message = "Deprecated")
fun <T> logDebug(tag: String, message: T) = Log.d(tag, "$message")

@Deprecated(message = "Deprecated")
fun <T> logError(value: T) = Log.e("logError", "$value")

@Deprecated(message = "Deprecated")
fun <T> logError(tag: String, message: T) = Log.e(tag, "$message")