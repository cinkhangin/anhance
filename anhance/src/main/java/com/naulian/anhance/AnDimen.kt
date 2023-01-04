@file:Suppress("unused")

package com.naulian.anhance

import android.content.res.Resources

fun Float.toPx() = toInt().toPx()
fun Float.toDp() = toInt().toDp()

fun Int.toPx(): Float {
    val resource = Resources.getSystem()
    val density = resource.displayMetrics.density
    return this * (density / 160)
}

fun Int.toDp(): Float {
    val resources = Resources.getSystem()
    val density = resources.displayMetrics.density
    return this / (density / 160)
}

