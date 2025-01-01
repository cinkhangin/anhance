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

fun roundIntOf(float: Float) = float.roundToInt()
fun roundLongOf(float: Float) = float.roundToLong()

//dimen
fun pxOf(int: Int) = int.pxValueOfDp()
fun dpOf(int : Int) = int.dpValueOfPx()
fun pxOf(float: Float) = float.pxValueOfDp()
fun dpOf(float: Float) = float.dpValueOfPx()

