@file:Suppress("unused")

package com.naulian.anhance

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

fun Activity.showToast(message: String) =
    showTextToast(this, message)

fun Activity.showKeyboard() =
    WindowInsetsControllerCompat(window, window.decorView)
        .show(WindowInsetsCompat.Type.ime())

fun Activity.hideKeyboard() =
    WindowInsetsControllerCompat(window, window.decorView)
        .hide(WindowInsetsCompat.Type.ime())


fun Activity.openTelegramUser(username: String) {
    val telegram = Intent(Intent.ACTION_VIEW, Uri.parse("tg://resolve?domain=$username"))
    telegram.setPackage("org.telegram.messenger")
    try {
        startActivity(telegram)
    } catch (e: ActivityNotFoundException) {
        showToast("Telegram not installed or username not found")
    }
}

