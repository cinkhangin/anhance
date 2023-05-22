@file:Suppress("unused")

package com.naulian.anhance

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException

fun readImage(path: String, fileName: String): Bitmap? {
    return try {
        val file = File(path, fileName)
        BitmapFactory.decodeStream(FileInputStream(file))
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
        null
    }
}