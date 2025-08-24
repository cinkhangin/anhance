package com.naulian.anhance

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import androidx.core.net.toUri

fun Context.openTelegramUser(username: String) {
    val telegram = Intent(Intent.ACTION_VIEW, "tg://resolve?domain=$username".toUri())
    telegram.setPackage("org.telegram.messenger")
    try {
        startActivity(telegram)
    } catch (e: ActivityNotFoundException) {
        showToast("Telegram not installed or username not found")
    }
}

fun Context.openMessenger(pageId: String) {
    try {
        val intent = Intent(
            Intent.ACTION_VIEW,
            "fb-messenger://user/$pageId".toUri()
        )
        startActivity(intent)
    } catch (e: Exception) {
        // Facebook Messenger app is not installed, open Messenger in a web browser
        val intent = Intent(
            Intent.ACTION_VIEW,
            "https://www.facebook.com/messages/t/$pageId".toUri()
        )
        startActivity(intent)
    }
}