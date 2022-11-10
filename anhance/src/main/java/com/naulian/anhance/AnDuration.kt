@file:Suppress("unused")

package com.naulian.anhance

//make units 3.sec == 3000 == 3 * SECOND
val Int.second get() = SECOND * this
val Int.minute get() = MINUTE * this
val Int.hour get() = HOUR * this
val Int.day get() = DAY * this
val Int.week get() = WEEK * this
val Int.month get() = MONTH * this
val Int.year get() = YEAR * this

fun secondOf(int : Int) = int.second
fun minuteOf(int: Int) = int.minute
fun hourOf(int: Int) = int.hour
fun dayOf(int: Int) = int.day
fun weekOf(int: Int) = int.week
fun monthOf(int: Int) = int.month
fun yearOf(int: Int) = int.year

//make units 3L.sec == 3000 == 3 * SECOND
val Long.second get() = SECOND * this
val Long.minute get() = MINUTE * this
val Long.hour get() = HOUR * this
val Long.day get() = DAY * this
val Long.week get() = WEEK * this
val Long.month get() = MONTH * this
val Long.year get() = YEAR * this

fun secondOf(long : Long) = long.second
fun minuteOf(long: Long) = long.minute
fun hourOf(long: Long) = long.hour
fun dayOf(long: Long) = long.day
fun weekOf(long: Long) = long.week
fun monthOf(long: Long) = long.month
fun yearOf(long: Long) = long.year