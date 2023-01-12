@file:Suppress("unused")

package com.naulian.anhance

//make units 3.sec == 3000 == 3 * SECOND
val Int.seconds get() = SECOND * this
val Int.minutes get() = MINUTE * this
val Int.hours get() = HOUR * this
val Int.days get() = DAY * this
val Int.weeks get() = WEEK * this
val Int.months get() = MONTH * this
val Int.years get() = YEAR * this

fun secondOf(int : Int) = int.seconds
fun minuteOf(int: Int) = int.minutes
fun hourOf(int: Int) = int.hours
fun dayOf(int: Int) = int.days
fun weekOf(int: Int) = int.weeks
fun monthOf(int: Int) = int.months
fun yearOf(int: Int) = int.years

//make units 3L.sec == 3000 == 3 * SECOND
val Long.seconds get() = SECOND * this
val Long.minutes get() = MINUTE * this
val Long.hours get() = HOUR * this
val Long.days get() = DAY * this
val Long.weeks get() = WEEK * this
val Long.months get() = MONTH * this
val Long.years get() = YEAR * this

fun secondOf(long : Long) = long.seconds
fun minuteOf(long: Long) = long.minutes
fun hourOf(long: Long) = long.hours
fun dayOf(long: Long) = long.days
fun weekOf(long: Long) = long.weeks
fun monthOf(long: Long) = long.months
fun yearOf(long: Long) = long.years

fun last(long: Long): Long = millisOfNow - long
