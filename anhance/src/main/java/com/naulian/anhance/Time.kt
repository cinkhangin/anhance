@file:Suppress("unused")

package com.naulian.anhance

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn
import java.text.SimpleDateFormat
import java.util.Locale

//Constants
const val YEAR: Long = 31_536_000_000L //365 days
const val MONTH: Long = 2_592_000_000L //30 days
const val WEEK: Long = 604_800_800L //7 days
const val DAY: Long = 86_400_000L
const val HOUR: Long = 3_600_000L
const val MINUTE: Long = 60_000L
const val SECOND: Long = 1000L
const val MILLI: Long = 1L

val Long.secondCount get() = this safeDiv SECOND
val Long.minuteCount get() = this safeDiv MINUTE
val Long.hourCount get() = this safeDiv HOUR
val Long.dayCount get() = this safeDiv DAY
val Long.weekCount get() = this safeDiv WEEK
val Long.monthCount get() = this safeDiv MONTH
val Long.yearCount get() = this safeDiv YEAR

//extract units like 35 minutes or 19 hours
val Long.leftSecond get() = this.secondCount % 60
val Long.leftMinute get() = this.minuteCount % 60
val Long.leftHour get() = this.hourCount % 24
val Long.leftDay get() = this.dayCount % 365
val Long.leftYear get() = this.yearCount

private val Long.secondOrEmpty get() = if (this > 0) "${this}s" else ""
private val Long.minuteOrEmpty get() = if (this > 0) "${this}m" else ""
private val Long.hourOrEmpty get() = if (this > 0) "${this}h" else ""
private val Long.dayOrEmpty get() = if (this > 0) "${this}ds" else ""
private val Long.yearOrEmpty get() = if (this > 0) "${this}y" else ""

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

val millisOfNow get() = System.currentTimeMillis()
val intOfDay get() = localDate.dayOfMonth
val intOfMonth get() = localDate.monthNumber
val intOfYear get() = localDate.year

val Long.timeLeft get() = if (this < millisOfNow) 0L else this - millisOfNow
val Long.isTimeLeft get() = this.timeLeft > 0
val Long.timeGap get() = millisOfNow - this

fun Long.formatWith(pattern: String): String {
    return SimpleDateFormat(pattern, Locale.getDefault()).format(this)
}

//format examples : 16m, 1h, 52s
fun Long.formatAgo(): String {
    val ago = millisOfNow - this
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

fun Long.formatLeft(withSec: Boolean = true): String {
    val sec = this.leftSecond.secondOrEmpty
    val min = this.leftMinute.minuteOrEmpty
    val hr = this.leftHour.hourOrEmpty
    val day = this.leftDay.dayOrEmpty
    val year = this.leftYear.yearOrEmpty
    return if (withSec) "$year$day$hr$min$sec".trimMargin()
    else "$year$day$hr$min"
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

fun Long.formatClock(): String {
    val secString = this.leftSecond.toClockString
    val minString = this.leftMinute.toClockString
    val hrString = this.leftHour.toClockString
    val dayString = this.leftDay.toDayString
    val yearString = this.leftYear.toYearString

    val timeString = "$yearString $dayString $hrString $minString $secString"
    val timeArray = timeString.split(" ")

    return "${timeArray[0]}${timeArray[1]}"
}

fun Long.formatDuration(): String {
    val units = arrayListOf(
        "${this.leftYear}y",
        "${this.leftDay}d",
        "${this.leftHour}hr",
        "${this.leftMinute}m",
        "${this.leftSecond}s",
    )

    val output = loopForString(units){
        val empty = it.startsWith("0")
        if(empty) "" else " $it"
    }
    return trim(output)
}

val clockKey get() = AnClock.createKey
object AnClock{
    val createKey get() = millisOfNow.formatWith(AnPattern.DATE_KEY)
}


