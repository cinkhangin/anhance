@file:Suppress("unused")

package com.naulian.anhance

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.webkit.MimeTypeMap
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.P)
fun Uri.toBitmap(context: Context) : Bitmap {
    val resolver = context.contentResolver
    val source = ImageDecoder.createSource(resolver, this)
    return ImageDecoder.decodeBitmap(source)
}

fun Uri.fileExtension(context: Context): String? {
    val resolver = context.contentResolver
    val type = resolver.getType(this)
    val mimeType = MimeTypeMap.getSingleton()
    return mimeType.getExtensionFromMimeType(type)
}