@file:Suppress("unused")

package com.ckgin.anhance

import android.graphics.Color
import android.util.Log


fun ifNotEmpty(string: String, action: (String) -> Unit) {
    if (string.isNotEmpty()) action(string)
}

fun ifEmpty(string: String, action: () -> Unit) {
    if (string.isEmpty()) action()
}

inline fun <T> loopForValue(
    range: IntRange, startValue: T,
    block: (Int, T) -> T
): T {
    var value: T = startValue
    for (i in range) {
        value = block(i, value)
    }

    return value
}

inline fun <T> forEach(list: Iterable<T>, action: (T) -> Unit) {
    for (element in list) action(element)
}

fun String.toColorInt(): Int {
    if (!startsWith("#")) {
        Log.e("toColorInt", "Error : '$this' is not a hex string")
        return Color.WHITE
    }

    return try {
        val hex = when (length) {
            4, 5 -> "#" + drop(1).flatMap { listOf(it, it) }.joinToString("")
            7, 9 -> this
            else -> return Color.WHITE
        }
        Color.parseColor(hex)
    } catch (_: IllegalArgumentException) {
        Color.WHITE
    }
}
