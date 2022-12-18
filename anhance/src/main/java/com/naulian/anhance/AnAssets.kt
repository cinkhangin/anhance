@file:Suppress("unused")

package com.naulian.anhance

import android.content.Context
import android.content.res.AssetFileDescriptor
import java.io.File
import java.io.FileOutputStream
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

fun Context.assetFilePath(assetName: String): String? {
    val file = File(filesDir, assetName)
    return try {
        assets.open(assetName).use { ins ->
            FileOutputStream(file).use { os ->
                val buffer = ByteArray(4 * 1024)
                var read: Int
                while (ins.read(buffer).also { read = it } != -1) {
                    os.write(buffer, 0, read)
                }
                os.flush()
            }
            return file.absolutePath
        }
    } catch (e: IOException) {
        logError("anhance : cannot process file path for $assetName")
        null
    }
}