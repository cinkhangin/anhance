@file:Suppress("unused")

package com.naulian.anhance

import android.app.Activity
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

fun Activity.showToast(message: String) =
    showTextToast(this, message)

fun Activity.showKeyboard() =
    WindowInsetsControllerCompat(window, window.decorView)
        .show(WindowInsetsCompat.Type.ime())

fun Activity.hideKeyboard() =
    WindowInsetsControllerCompat(window, window.decorView)
        .hide(WindowInsetsCompat.Type.ime())

