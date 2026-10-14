@file:Suppress("unused")

package com.ckgin.anhance

import android.content.Context
import android.content.res.AssetFileDescriptor
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


fun Context.readStringAsset(filename: String): String = runCatching {
    assets.open(filename).bufferedReader().use { it.readText() }
}.fold(onSuccess = { it }, onFailure = { "" })


fun Context.readStringAsset(
    filename: String, result: (Result<String>) -> Unit
) {
    runCatching {
        assets.open(filename)
            .bufferedReader()
            .use { it.readText() }
    }.let(result)
}

fun readStringAsset(context: Context, filename: String) = runCatching {
    context.assets.open(filename)
        .bufferedReader()
        .use { it.readText() }
}

fun Context.readAsset(filename: String): AssetFileDescriptor? {
    return try {
        assets.openFd(filename)
    } catch (e: IOException) {
        null
    }
}

fun Context.readAssetFd(filename: String, action: (Result<AssetFileDescriptor>) -> Unit) {
    val result = try {
        Result.success(assets.openFd(filename))
    } catch (exception: IOException) {
        Result.failure(exception)
    }
    action(result)
}

fun Context.assetFilePath(assetName: String): String {
    var temporary: File? = null
    return try {
        val root = filesDir.canonicalFile
        val file = File(root, assetName).canonicalFile
        if (!file.path.startsWith(root.path + File.separator)) {
            throw IOException("Asset path must stay inside filesDir")
        }
        val parent = file.parentFile ?: throw IOException("Missing asset directory")
        if (!parent.isDirectory && !parent.mkdirs()) {
            throw IOException("Cannot create asset directory")
        }
        assets.open(assetName).use { ins ->
            val copy = File.createTempFile("asset", ".tmp", parent)
            temporary = copy
            FileOutputStream(copy).use { os ->
                ins.copyTo(os)
            }
            if (!copy.renameTo(file)) throw IOException("Cannot replace asset file")
        }
        file.absolutePath
    } catch (_: IOException) {
        ""
    } finally {
        temporary?.delete()
    }
}
