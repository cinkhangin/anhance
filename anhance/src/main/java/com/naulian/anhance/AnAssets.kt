@file:Suppress("unused")

package com.naulian.anhance

import android.content.Context
import android.content.res.AssetFileDescriptor
import java.io.IOException

fun Context.readStringAsset(filename: String): String {
    var string = ""
    try {
        string = assets.open(filename)
            .bufferedReader()
            .use { it.readText() }
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return string
}

fun Context.readAsset(filename: String): AssetFileDescriptor? {
    return try {
        assets.openFd(filename)
    } catch (e: IOException) {
        null
    }
}