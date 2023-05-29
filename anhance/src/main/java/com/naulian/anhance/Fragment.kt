@file:Suppress("unused")

package com.naulian.anhance

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.activity.addCallback
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
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

val Fragment.isInternetAvailable get() = requireContext().isInternetAvailable

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

fun Fragment.observeBackPressed(action: () -> Unit) {
    requireActivity().onBackPressedDispatcher
        .addCallback(viewLifecycleOwner) { action() }
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
fun Fragment.createGLManager(defaultSpan: Int = 2, otherSpan: Int = 2): GridLayoutManager {
    val spanCount = if (isLandscape) otherSpan else defaultSpan
    return GridLayoutManager(requireContext(), spanCount)
}

fun Fragment.createSGManager(
    defaultSpan: Int = 2, otherSpan: Int = 0, vertical: Boolean
): StaggeredGridLayoutManager {
    val newOtherSpan = if (otherSpan == 0) defaultSpan else otherSpan
    val spanCount = if (isLandscape) newOtherSpan else defaultSpan
    val orientation = if (vertical) StaggeredGridLayoutManager.VERTICAL
    else StaggeredGridLayoutManager.HORIZONTAL
    return StaggeredGridLayoutManager(spanCount, orientation)
}

//navigation
val Fragment.navigationGraph get() = findNavController().graph
fun Fragment.navigateTo(direction: NavDirections) {
    findNavController().navigate(direction)
}

fun Fragment.navigateTo(actionId: Int) {
    findNavController().navigate(actionId)
}

fun NavDirections.navigateWith(fragment: Fragment) {
    fragment.findNavController().navigate(this)
}

fun Fragment.popBackStack() {
    findNavController().popBackStack()
}

fun Fragment.setUpPopBackStack(toolbar: MaterialToolbar) {
    toolbar.setNavigationOnClickListener { popBackStack() }
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

fun <T> loadUi(binding: T, action: T.() -> Unit) = action(binding)

fun Fragment.loadData(block: suspend CoroutineScope.() -> Unit) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            block(this@repeatOnLifecycle)
        }
    }
}

//Tools
fun Fragment.permissionRequestResult(action: (granted: Boolean) -> Unit):
        ActivityResultLauncher<String> {
    return registerForActivityResult(ActivityResultContracts.RequestPermission()) { action(it) }
}

fun Fragment.isPermissionGranted(
    forResult: ActivityResultLauncher<String>,
    requestedPermission: String
): Boolean {
    when {
        requireContext().isPermissionGranted(requestedPermission) -> return true
        shouldShowRequestPermissionRationale(requestedPermission) -> forResult.launch(requestedPermission)
        else -> forResult.launch(requestedPermission)
    }

    return false
}

fun Fragment.openGallery(
    request: ActivityResultLauncher<String>,
    content: ActivityResultLauncher<Intent>
) {
    if (isPermissionGranted(request, Manifest.permission.READ_EXTERNAL_STORAGE)) {
        launchGallery(content)
    }
}

fun launchGallery(content: ActivityResultLauncher<Intent>) {
    val intent = Intent(Intent.ACTION_PICK)
    intent.type = "image/*"
    content.launch(intent)
}

fun Fragment.activityResultCallBack(block: (uri: Uri?) -> Unit): ActivityResultLauncher<Intent> {
    val action = ActivityResultContracts.StartActivityForResult()
    return registerForActivityResult(action) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.let { block(it.data) }
        }
    }
}

fun Fragment.setLightStatusBar(light: Boolean) {
    requireActivity().setLightStatusBar(light)
}

@Suppress("DEPRECATION")
fun Activity.setLightStatusBar(light: Boolean) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        window.insetsController?.apply {
            val lightAppearance = WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            val appearance = if (light) lightAppearance else 0

            setSystemBarsAppearance(appearance, lightAppearance)
            show(WindowInsets.Type.statusBars())
        }
    } else {
        val appearance = if (light) View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR else 0
        window.decorView.systemUiVisibility = appearance
    }
}

fun Fragment.openTelegramUser(username: String) {
    requireActivity().openTelegramUser(username)
}

fun Fragment.openMic(request : ActivityResultLauncher<String>, action: ()-> Unit){
    if (isPermissionGranted(request, Manifest.permission.RECORD_AUDIO)) {
        action()
    }
}