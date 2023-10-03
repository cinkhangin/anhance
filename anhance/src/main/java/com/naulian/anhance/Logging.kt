@file:Suppress("unused")

package com.naulian.anhance

import android.util.Log


fun <T> logInfo(value: T) = Log.i("Info", "$value")
fun <T> logInfo(tag: String, message: T) = Log.i(tag, "$message")

fun <T> logDebug(value: T) = Log.d("Debug", "$value")
fun <T> logDebug(tag: String, message: T) = Log.d(tag, "$message")

fun <T> logError(value: T) = Log.e("logError", "$value")
fun <T> logError(tag: String, message: T) = Log.e(tag, "$message")