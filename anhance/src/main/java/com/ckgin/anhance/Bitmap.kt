@file:Suppress("unused")

package com.ckgin.anhance

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import java.io.File
import java.io.IOException

fun Bitmap.saveAsJpeg(context: Context) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) save(context)
    else saveQ(context)
}

@RequiresApi(Build.VERSION_CODES.Q)
private fun Bitmap.saveQ(context: Context) {
    val resolver = context.contentResolver
    var imageUri: android.net.Uri? = null
    try {
        val values = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "image_$millisOfNow.jpeg")
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            put(MediaStore.MediaColumns.RELATIVE_PATH, "${Environment.DIRECTORY_PICTURES}/images")
            put(MediaStore.MediaColumns.IS_PENDING, 1)
        }
        val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            ?: throw IOException("Cannot create image")
        imageUri = uri
        val stream = resolver.openOutputStream(uri) ?: throw IOException("Cannot open image output stream")
        stream.use {
            if (!compress(Bitmap.CompressFormat.JPEG, 100, it)) throw IOException("Cannot compress image")
        }
        val published = ContentValues().apply { put(MediaStore.MediaColumns.IS_PENDING, 0) }
        if (resolver.update(uri, published, null, null) <= 0) throw IOException("Cannot publish image")
        context.showToast("Image Saved")
    } catch (exception: Exception) {
        imageUri?.let { uri ->
            try {
                resolver.delete(uri, null, null)
            } catch (cleanup: Exception) {
                exception.addSuppressed(cleanup)
            }
        }
        context.showToast("Image Not Saved: ${exception.message}")
    }
}

private fun Bitmap.save(context: Context) {
    var savedFile: File? = null
    try {
        val directory = File(Environment.getExternalStorageDirectory(), "images")
        if (!directory.isDirectory && !directory.mkdirs()) throw IOException("Cannot create image directory")
        val file = File.createTempFile("image_", ".jpeg", directory)
        savedFile = file
        file.outputStream().use {
            if (!compress(Bitmap.CompressFormat.JPEG, 100, it)) throw IOException("Cannot compress image")
        }
        context.showToast("Image Saved")
    } catch (exception: Exception) {
        savedFile?.delete()
        context.showToast("Image Not Saved: ${exception.message}")
    }
}
