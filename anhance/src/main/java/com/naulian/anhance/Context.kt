@file:Suppress("unused")

package com.naulian.anhance

import android.content.ClipboardManager
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

private const val CLIPBOARD = Context.CLIPBOARD_SERVICE
private const val CONNECTIVITY = Context.CONNECTIVITY_SERVICE

private const val NETWORK_CELLULAR = NetworkCapabilities.TRANSPORT_CELLULAR
private const val NETWORK_WIFI = NetworkCapabilities.TRANSPORT_WIFI
private const val NETWORK_ETHERNET = NetworkCapabilities.TRANSPORT_WIFI
private val NetworkCapabilities.hasCellular get() = hasTransport(NETWORK_CELLULAR)
private val NetworkCapabilities.hasEthernet get() = hasTransport(NETWORK_ETHERNET)
private val NetworkCapabilities.hasWifi get() = hasTransport(NETWORK_WIFI)

private fun Context.getService(service: String) =
    this.getSystemService(service)

val Context.clipboardService get() : Any = this.getService(CLIPBOARD)
val Context.connectivityService get() : Any = this.getService(CONNECTIVITY)

val Context.clipboardManager get() = this.clipboardService as ClipboardManager
val Context.connectivityManager get() = this.connectivityService as ConnectivityManager

val Context.networkCapabilities
    get() : NetworkCapabilities? {
        val manager = connectivityManager
        return manager.getNetworkCapabilities(manager.activeNetwork)
    }

val Context.isInternetAvailable
    get() : Boolean {
        val availability = networkCapabilities?.let {
            it.hasWifi || it.hasCellular || it.hasEthernet
        }
        return availability ?: false
    }

fun Context.showToast(message: String) {
    showTextToast(this, message)
}
