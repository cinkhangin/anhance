@file:Suppress("unused")

package com.naulian.anhance

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.naulian.anhance.objects.AnLoadingDialog
import com.naulian.anhance.objects.AnPermission
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

val Fragment.isInternetAvailable get() = requireContext().isInternetAvailable

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

fun Fragment.createLLManager(
    vertical: Boolean = true,
    reverse: Boolean = false,
    fromEnd: Boolean = false
): LinearLayoutManager {
    val orientation = if (vertical) RecyclerView.VERTICAL
    else RecyclerView.HORIZONTAL
    return LinearLayoutManager(requireContext(), orientation, reverse)
        .apply { stackFromEnd = fromEnd }
}

val Fragment.configuration get() : Configuration = resources.configuration
val Fragment.orientation get() = configuration.orientation
val Fragment.isLandscape get() = orientation == Configuration.ORIENTATION_LANDSCAPE
fun Fragment.createGridLayoutManager(defaultSpan: Int, otherSpan: Int): GridLayoutManager {
    val spanCount = if (isLandscape) otherSpan else defaultSpan
    return GridLayoutManager(requireContext(), spanCount)
}

//navigation
val Fragment.navigationGraph get() = findNavController().graph
fun Fragment.navigateTo(direction: NavDirections) {
    findNavController().navigate(direction)
}
fun Fragment.navigateTo(actionId: Int) {
    findNavController().navigate(actionId)
}
fun Fragment.popBackStack(){
    findNavController().popBackStack()
}

fun Fragment.updateStartDestination(id: Int) {
    if (navigationGraph.startDestinationId == id) return
    else navigationGraph.setStartDestination(id)
}

//String
fun Fragment.copyString(string: String) {
    string.copy(requireContext())
}

//Architecture
inline fun Fragment.initialize(block: Context.() -> Unit) {
    block(requireContext())
}

inline fun <T> Fragment.loadUi(binding: T, block: T.() -> Unit) {
    binding.apply { block(this) }
}

fun Fragment.loadData(block: suspend CoroutineScope.() -> Unit) {
    fragmentOnStartScope { block(this) }
}

//Tools
fun Fragment.openGallery(block: (uri: Uri?) -> Unit) {
    val action = ActivityResultContracts.StartActivityForResult()
    val forResult = registerForActivityResult(action) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.let { block(it.data) }
        }
    }
    launchDeviceGallery(forResult)
}

private fun Fragment.launchDeviceGallery(forResult: ActivityResultLauncher<Intent>) {
    if (requireActivity().askForPermission(AnPermission.READ_STORAGE)) {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        forResult.launch(intent)
    }
}
