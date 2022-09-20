@file:Suppress("unused")

package com.naulian.anhance

import android.content.ClipData
import android.content.Context
import androidx.core.text.HtmlCompat
import androidx.core.text.isDigitsOnly

fun String.toSafeInt() =
    if (this.isDigitsOnly()) this.toInt() else 0

fun String.toHtml() =
    HtmlCompat.fromHtml(this, HtmlCompat.FROM_HTML_MODE_COMPACT)

val String.clipData get() : ClipData =
    ClipData.newPlainText("clip", this)
fun String.copy(context: Context) {
    context.clipboardManager.setPrimaryClip(this.clipData)
    context.showToast(this)
}