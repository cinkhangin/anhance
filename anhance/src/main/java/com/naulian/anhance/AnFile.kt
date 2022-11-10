@file:Suppress("unused")

package com.naulian.anhance

import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap

fun Uri.fileExtension(context: Context): String? {
    val contentResolver = context.contentResolver
    val type = contentResolver.getType(this)
    val mimeType = MimeTypeMap.getSingleton()
    return mimeType.getExtensionFromMimeType(type)
}