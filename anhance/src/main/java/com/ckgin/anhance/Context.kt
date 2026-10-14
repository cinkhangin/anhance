@file:Suppress("unused")

package com.ckgin.anhance

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

val Context.networkCapabilities
    get() : NetworkCapabilities? {
        val manager = connectivityManager
        return manager.getNetworkCapabilities(manager.activeNetwork)
    }

val Context.isInternetAvailable
    get() : Boolean {
        val availability = networkCapabilities?.let {
            it.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                it.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        }
        return availability ?: false
    }
