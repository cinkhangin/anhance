@file:Suppress("unused")

package com.naulian.anhance

import android.content.res.Resources

fun Int.pxValueOfDp() = toFloat().pxValueOfDp()
fun Int.dpValueOfPx() = toFloat().dpValueOfPx()

fun Float.pxValueOfDp(): Float {
    val resource = Resources.getSystem()
    val density = resource.displayMetrics.density
    return this * density
}

fun Float.dpValueOfPx(): Float {
    val resources = Resources.getSystem()
    val density = resources.displayMetrics.density
    return this / density
}

