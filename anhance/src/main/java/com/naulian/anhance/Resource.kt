@file:Suppress("unused")

package com.naulian.anhance

import android.content.Context
import android.net.Uri

fun Int.toUri(context: Context): Uri {
    val packageName = context.packageName
    return Uri.parse("android.resource://$packageName/$this")
}