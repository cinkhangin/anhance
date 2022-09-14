@file:Suppress("unused")

package com.naulian.anhance

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

fun Fragment.repeatOnLifeCycleScope(block: (scope : CoroutineScope) -> Unit) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            block(this)
        }
    }
}

fun Fragment.requireCoroutineScope(block: suspend CoroutineScope.() -> Unit) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            block(this@repeatOnLifecycle)
        }
    }
}

fun Fragment.showToast(message: String) {
    showTextToast(requireContext(), message)
}

private fun Fragment.setStatusBarColor(color: Int) {
    requireActivity().window.statusBarColor = color
}

fun Fragment.setStatusBarColorResource(colorRes: Int) {
    val color = ContextCompat.getColor(requireContext(), colorRes)
    setStatusBarColor(color)
}

fun Fragment.restoreStatusBarColor(colorId: Int) {
    setStatusBarColorResource(colorId)
}

fun Fragment.isInternetAvailable(): Boolean {
    val connectivityManager = requireContext()
        .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val capabilities = connectivityManager
        .getNetworkCapabilities(connectivityManager.activeNetwork)

    capabilities?.let {
        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
    }
    return false
}

fun Fragment.getColor(resId: Int): Int {
    return ContextCompat.getColor(requireContext(), resId)
}

fun Fragment.showKeyboard() = requireActivity().showKeyboard()
fun Fragment.hideKeyboard() = requireActivity().hideKeyboard()

fun Fragment.showLoadingDialog(message: String = "Please wait") {
    AnLoadingDialog.show(requireContext(), message)
}

fun Fragment.dismissLoadingDialog() {
    AnLoadingDialog.dismiss()
}