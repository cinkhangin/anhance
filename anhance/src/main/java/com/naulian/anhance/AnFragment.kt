@file:Suppress("unused")

package com.naulian.anhance

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun Fragment.repeatOnLifeCycleScope(actionBlock: (scope: CoroutineScope) -> Unit) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            actionBlock(this)
        }
    }
}

fun Fragment.showToast(message: String) {
    showTextToast(requireContext(), message)
}

fun Activity.showToast(message: String) {
    showTextToast(this, message)
}

fun Context.showToast(message: String) {
    showTextToast(this, message)
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

    val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

    capabilities?.let {
        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
    }
    return false
}

fun showTextToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}


fun Fragment.getColor(resId: Int): Int {
    return ContextCompat.getColor(requireContext(), resId)
}

fun Fragment.showKeyboard() = requireActivity().showKeyboard()
fun Fragment.hideKeyboard() = requireActivity().hideKeyboard()

fun Activity.showKeyboard() =
    WindowInsetsControllerCompat(window, window.decorView)
        .show(WindowInsetsCompat.Type.ime())

fun Activity.hideKeyboard() =
    WindowInsetsControllerCompat(window, window.decorView)
        .hide(WindowInsetsCompat.Type.ime())

fun Fragment.showLoadingDialog(message: String = "Please wait") {
    LoadingDialog.show(requireContext(), message)
}

fun Fragment.dismissLoadingDialog() {
    LoadingDialog.dismiss()
}