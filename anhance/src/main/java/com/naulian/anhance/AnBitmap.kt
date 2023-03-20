@file:Suppress("unused")

package com.naulian.anhance

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import java.io.File
import java.io.FileOutputStream

fun Bitmap.saveAsJpeg(context: Context) {
    val android10 = Build.VERSION_CODES.Q
    if (Build.VERSION.SDK_INT < android10) save(context)
    else saveQ(context)
}

@RequiresApi(Build.VERSION_CODES.Q)
private fun Bitmap.saveQ(context: Context) {
    val resolver = context.contentResolver
    val filename = "image_$millisOfNow.jpeg"

    ContentValues().apply {
        val directory = Environment.DIRECTORY_PICTURES
        val contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val pathName = "$directory/images"
        put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        put(MediaStore.MediaColumns.RELATIVE_PATH, pathName)
        val imageUri = resolver.insert(contentUri, this)

        try {
            imageUri?.let {
                val os = resolver.openOutputStream(imageUri)
                compress(Bitmap.CompressFormat.JPEG, 100, os)
                os?.flush()
                os?.close()
                context.showToast("Image Saved")
            }
        } catch (e: Exception) {
            context.showToast("Image Not Not  Saved: ${e.message}")
            e.printStackTrace()
            logError("AnBitmap saveQ",e.message)
        }
    }
}

private fun Bitmap.save(context: Context) {
    val root = Environment.getExternalStorageDirectory().toString()
    val fileDir = File("$root/images")

    if (!fileDir.exists()) {
        fileDir.mkdirs()
    }

    val filename = "image_$millisOfNow.jpeg"
    val file = File(fileDir, filename)
    if (file.exists()) file.delete()

    try {
        val os = FileOutputStream(file)
        compress(Bitmap.CompressFormat.JPEG, 100, os)
        os.flush()
        os.close()
        context.showToast("Image Saved")
    } catch (e: Exception) {
        context.showToast("Image Not Not  Saved: ${e.message}")
        e.printStackTrace()
        logError("AnBitmap save",e.message)
    }
}