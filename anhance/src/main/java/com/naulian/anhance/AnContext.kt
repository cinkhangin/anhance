@file:Suppress("unused")

package com.naulian.anhance

import android.content.ClipboardManager
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

private const val CLIPBOARD = Context.CLIPBOARD_SERVICE
private const val CONNECTIVITY = Context.CONNECTIVITY_SERVICE

private fun Context.getService(service: String) =
    this.getSystemService(service)

val Context.clipboardService get() : Any = this.getService(CLIPBOARD)
val Context.connectivityService get() : Any = this.getService(CONNECTIVITY)

val Context.clipboardManager get() = this.clipboardService as ClipboardManager
val Context.connectivityManager get() = this.connectivityService as ConnectivityManager

val Context.networkCapabilities get() : NetworkCapabilities?{
    val manager = this.connectivityManager
    return manager.getNetworkCapabilities(manager.activeNetwork)
}

val Context.isInternetAvailable get() : Boolean {
    this.networkCapabilities?.let {
        return it.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                it.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                it.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
    }
    return false
}

fun Context.showToast(message: String) {
    showTextToast(this, message)
}
