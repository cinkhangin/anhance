@file:Suppress("unused")

package com.naulian.anhance

import android.content.Context
import java.io.IOException

fun Context.readAssets(filename: String): String {
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