package com.ckgin.anhance

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.net.toUri

fun Context.openTelegramUser(username: String) {
    val uri = Uri.Builder().scheme("tg").authority("resolve")
        .appendQueryParameter("domain", username).build()
    val telegram = Intent(Intent.ACTION_VIEW, uri)
    telegram.setPackage("org.telegram.messenger")
    try {
        openExternalActivity(telegram)
    } catch (e: ActivityNotFoundException) {
        showToast("Telegram not installed or username not found")
    }
}

fun Context.openMessenger(pageId: String) {
    try {
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.Builder().scheme("fb-messenger").authority("user").appendPath(pageId).build()
        )
        openExternalActivity(intent)
    } catch (e: ActivityNotFoundException) {
        // Facebook Messenger app is not installed, open Messenger in a web browser
        val intent = Intent(
            Intent.ACTION_VIEW,
            "https://www.facebook.com/messages/t".toUri().buildUpon().appendPath(pageId).build()
        )
        try {
            openExternalActivity(intent)
        } catch (_: ActivityNotFoundException) {
            showToast("Messenger and web browser are not installed")
        }
    }
}

private fun Context.openExternalActivity(intent: Intent) {
    if (this !is Activity) intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(intent)
}
