@file:Suppress("unused")

package com.naulian.anhance

import android.content.ClipboardManager
import android.content.Context

val Context.clipboardService
    get() : Any = this.getSystemService(Context.CLIPBOARD_SERVICE)

val Context.clipboardManager
    get() = this.clipboardService as ClipboardManager


fun Context.showToast(message: String) {
    showTextToast(this, message)
}
