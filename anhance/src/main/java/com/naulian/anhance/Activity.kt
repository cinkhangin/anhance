@file:Suppress("unused")

package com.naulian.anhance

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri

fun Context.openTelegramUser(username: String) {
    val telegram = Intent(Intent.ACTION_VIEW, Uri.parse("tg://resolve?domain=$username"))
    telegram.setPackage("org.telegram.messenger")
    try {
        startActivity(telegram)
    } catch (e: ActivityNotFoundException) {
        showToast("Telegram not installed or username not found")
    }
}

