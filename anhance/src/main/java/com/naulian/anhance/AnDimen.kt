@file:Suppress("unused")

package com.naulian.anhance

import android.content.res.Resources
import android.util.TypedValue
import kotlin.math.roundToInt
import kotlin.math.roundToLong

private const val UNIT_DP = TypedValue.COMPLEX_UNIT_DIP
private const val UNIT_PX = TypedValue.COMPLEX_UNIT_PX
private const val UNIT_SP = TypedValue.COMPLEX_UNIT_SP

val Int.toPx get() = floatOf(this).toPx
val Int.toDp get() = floatOf(this).toDp

private val system get() = Resources.getSystem()
private val displayMetrics get() = system.displayMetrics
private val density get() = displayMetrics.density

val Float.toPx get() = (this * density).roundToInt()
val Float.toDp get() = (this / density).roundToInt()
val Float.toSP get() = (this / density).roundToInt()

