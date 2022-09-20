@file:Suppress("unused")

package com.naulian.anhance

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn
import java.text.SimpleDateFormat
import java.util.*

//Constants
const val YEAR: Long = 31_536_000_000L //365 days
const val MONTH: Long = 2_592_000_000L //30 days
const val WEEK: Long = 604_800_800L //7 days
const val DAY: Long = 86_400_000L
const val HOUR: Long = 3_600_000L
const val MINUTE: Long = 60_000L
const val SECOND: Long = 1000L
const val MILLI : Long = 1L

//get current millis
val millisNow get() = System.currentTimeMillis()
val millisOfNow get() = millisNow

//make units 3.sec == 3000 == 3 * SECOND
val Int.sec get() = SECOND * this
val Int.min get() = MINUTE * this
val Int.hr get() = HOUR * this
val Int.day get() = DAY * this
val Int.week get() = WEEK * this
val Int.month get() = MONTH * this
val Int.year get() = YEAR * this

//make units 3L.sec == 3000 == 3 * SECOND
val Long.sec get() = SECOND * this
val Long.min get() = MINUTE * this
val Long.hr get() = HOUR * this
val Long.day get() = DAY * this
val Long.week get() = WEEK * this
val Long.month get() = MONTH * this
val Long.year get() = YEAR * this

//extract units like 1038 seconds or 72 hours
val Long.toSecond get() = this safeDiv SECOND
val Long.toMinute get() = this safeDiv MINUTE
val Long.toHour get() = this safeDiv HOUR
val Long.toDay get() = this safeDiv DAY
val Long.toWeek get() = this safeDiv WEEK
val Long.toMonth get() = this safeDiv MONTH
val Long.toYear get() = this safeDiv YEAR

//extract units like 35 minutes or 19 hours
val Long.leftSecond get() = this.toSecond % 60
val Long.leftMinute get() = this.toMinute % 60
val Long.leftHour get() = this.toHour % 24
val Long.leftDay get() = this.toDay % 365
val Long.leftYear get() = this.toYear

// 3y10d 04:35:07
val Long.toClockString get() = if (this == 0L) "00" else this.to0String
val Long.toDayString get() = if (this == 0L) "" else "${this}d "
val Long.toYearString get() = if (this == 0L) "" else "${this}y"
val Int.to0String get() = if (this < 10) "0$this" else "$this"
val Long.to0String get() = if (this < 10) "0$this" else "$this"

private val systemClock get() = Clock.System
private val now get() = systemClock.now()
private val timeZone get() = TimeZone.currentSystemDefault()
private val localDate = systemClock.todayIn(timeZone)
private val localDateTime = now.toLocalDateTime(timeZone)
val intOfDay get() = localDate.dayOfMonth
val intOfMonth get() = localDate.monthNumber
val intOfYear get() = localDate.year

fun Long.formatWith(pattern : String) : String{
    return SimpleDateFormat(pattern, Locale.getDefault()).format(this)
}

//format examples : 16m, 1h, 52s
fun Long.formatAgo(): String {
    val ago = millisNow - this
    return when {
        ago > YEAR -> "${ago / YEAR}y"
        ago > MONTH -> "${ago / MONTH}mo"
        ago > DAY -> "${ago / DAY}d"
        ago > HOUR -> "${ago / HOUR}h"
        ago > MINUTE -> "${ago / MINUTE}m"
        ago > SECOND -> "${ago / SECOND}s"
        else -> "Now"
    }
}

//format examples : 00:33 , 01:16 , 4d 01:35:00
fun Long.formatTimer(): String {
    val secString = this.leftSecond.toClockString
    val minString = this.leftMinute.toClockString
    val hrString = this.leftHour.toClockString
    val dayString = this.leftDay.toDayString
    val yearString = this.leftYear.toYearString

    val rawTimeString = "$yearString$dayString$hrString:$minString:$secString"

    return rawTimeString.removePrefix(" 00:")
        .removePrefix("00:")
}

