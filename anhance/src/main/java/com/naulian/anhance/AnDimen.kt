@file:Suppress("unused")

package com.naulian.anhance

import android.content.Context
import android.content.res.Resources
import android.util.DisplayMetrics
import android.util.TypedValue
import kotlin.math.roundToInt
import kotlin.math.roundToLong

val Int.toFloat get() = this.toFloat()
val Int.toLong get() = this.toLong()

val Float.toInt get() = this.toInt()
val Float.toLong get() = this.toLong()
val Float.roundToInt get() = this.roundToInt()
val Float.roundToLong get() = this.roundToLong()

private const val UNIT_DP = TypedValue.COMPLEX_UNIT_DIP
private const val UNIT_PX = TypedValue.COMPLEX_UNIT_PX

val Int.toPx get() = this.toFloat.toPx
val Int.toDp get() = this.toFloat.toDp

val Float.toPx get() =
    (this * Resources.getSystem().displayMetrics.density).roundToInt()
val Float.toDp get() =
    (this / Resources.getSystem().displayMetrics.density).roundToInt()

private fun DisplayMetrics.applyDimen(type: Int, unit: Float) =
    TypedValue.applyDimension(type, unit, this)
