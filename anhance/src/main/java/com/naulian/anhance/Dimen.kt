@file:Suppress("unused")

package com.naulian.anhance

import android.content.res.Resources


val Float.px get() = toPx()
val Float.dp get() = toDp()
val Int.px get() = toPx()
val Int.dp get() = toDp()

fun Int.toPx() = toFloat().toPx()
fun Int.toDp() = toFloat().toDp()

fun Float.toPx(): Float {
    val resource = Resources.getSystem()
    val density = resource.displayMetrics.density
    return this * density
}

fun Float.toDp(): Float {
    val resources = Resources.getSystem()
    val density = resources.displayMetrics.density
    return this / density
}

