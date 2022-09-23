@file:Suppress("unused")

package com.naulian.anhance

import android.content.ClipData
import android.content.Context
import androidx.core.text.isDigitsOnly
import com.naulian.anhance.objects.AnText

fun String.toSafeInt() =
    if (this.isDigitsOnly()) this.toInt() else 0

private val String.clipData get() : ClipData =
    ClipData.newPlainText("clip", this)

fun String.copy(context: Context) {
    context.clipboardManager.setPrimaryClip(this.clipData)
    context.showToast(this)
}

fun String.censor() = AnText.censor(this)