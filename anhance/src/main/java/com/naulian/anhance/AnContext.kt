@file:Suppress("unused")

package com.naulian.anhance

import android.content.Context

fun Context.showToast(message: String) {
    showTextToast(this, message)
}