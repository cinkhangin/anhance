@file:Suppress("unused")

package com.naulian.anhance

import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

val Fragment.isInternetAvailable get() = requireContext().isInternetAvailable

fun Fragment.repeatOnLifeCycleScope(block: (scope: CoroutineScope) -> Unit) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            block(this)
        }
    }
}

fun Fragment.fragmentOnStartScope(block: suspend CoroutineScope.() -> Unit) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            block(this@repeatOnLifecycle)
        }
    }
}

fun Fragment.showToast(message: String) {
    showTextToast(requireContext(), message)
}

fun Fragment.setStatusBarColor(color: Int) {
    requireActivity().window.statusBarColor = color
}

fun Fragment.setStatusBarColorResource(resId: Int) {
    setStatusBarColor(getColor(resId))
}

fun Fragment.restoreStatusBarColor(resId: Int) {
    setStatusBarColorResource(resId)
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