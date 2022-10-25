@file:Suppress("unused")

package com.naulian.anhance

import android.util.Log

fun <T> logInfo(value: T){
    Log.i("logInfo", "$value")
}

fun <T> logDebug(value: T){
    Log.d("logDebug", "$value")
}

fun <T> logError(value: T){
    Log.e("logError", "$value")
}