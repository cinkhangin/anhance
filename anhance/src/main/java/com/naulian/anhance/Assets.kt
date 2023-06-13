@file:Suppress("unused")

package com.naulian.anhance

import android.content.Context
import android.content.res.AssetFileDescriptor
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

fun Context.readStringAsset(filename: String): String {
    return try {
         assets.open(filename)
            .bufferedReader()
            .use { it.readText() }
    } catch (e: IOException) {
        e.printStackTrace()
        ""
    }
}

fun Context.readStringAsset(filename: String, action: (Result<String>) -> Unit) {
    try {
        val string = assets.open(filename)
            .bufferedReader()
            .use { it.readText() }
        action(Result.success(string))
    } catch (ioException: IOException) {
        ioException.printStackTrace()
        action(Result.failure(ioException))
    }
}

fun Context.readAsset(filename: String): AssetFileDescriptor? {
    return try {
        assets.openFd(filename)
    } catch (e: IOException) {
        null
    }
}

fun Context.readAssetFd(filename: String, action: (Result<AssetFileDescriptor>) -> Unit) {
    try {
        action(Result.success(assets.openFd(filename)))
    } catch (exception: IOException) {
        exception.printStackTrace()
        action(Result.failure(exception))
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
        logError("AnAsset","cannot process file path for $assetName")
        null
    }
}
