@file:Suppress("unused")

package com.naulian.anhance

import android.content.Context
import android.net.Uri
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment

fun Int.toUri(context: Context): Uri {
    val packageName = context.packageName
    return Uri.parse("android.resource://$packageName/$this")
}

fun Fragment.getDrawable(resId: Int) =
    AppCompatResources.getDrawable(requireContext(), resId)