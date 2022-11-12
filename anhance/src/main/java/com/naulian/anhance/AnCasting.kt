@file:Suppress("unused")

package com.naulian.anhance

import kotlin.math.roundToInt
import kotlin.math.roundToLong

//pre
fun intOf(long: Long) = long.toInt()
fun intOf(string: String) = string.toSafeInt()
fun intOf(float: Float) = float.toInt()

fun longOf(int: Int) = int.toLong()

fun floatOf(int: Int) = int.toFloat()

fun rountIntOf(float: Float) = float.roundToInt()

fun roundLongOf(float: Float) = float.roundToLong()

//dimen
fun pxOf(int: Int) = int.toPx
fun dpOf(int : Int) = int.toDp
fun pxOf(float: Float) = float.toPx
fun dpOf(float: Float) = float.toDp

