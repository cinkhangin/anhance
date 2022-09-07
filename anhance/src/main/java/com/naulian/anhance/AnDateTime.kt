package com.naulian.anhance

import android.util.Log
import kotlinx.datetime.*

@Suppress("unused", "MemberVisibilityCanBePrivate")
object AnDateTime {
    private const val TAG = "AnDateTime"
    const val YEAR: Long = 217_728_000_000L
    const val MONTH: Long = 18_144_000_000L
    const val WEEK: Long = 604_800_800L
    const val DAY: Long = 86_400_000L
    const val HOUR: Long = 3_600_000L
    const val MINUTE: Long = 60_000L
    const val SECOND: Long =  1000L

    val systemClock get() = Clock.System
    val now get() = systemClock.now()
    val timeZone get() = TimeZone.currentSystemDefault()
    val localDate = systemClock.todayIn(timeZone)
    val localDateTime = now.toLocalDateTime(timeZone)

    val dayOfMonth get() = localDate.dayOfMonth
    val monthOfYear get() = localDate.monthNumber
    val year get() = localDate.year

    val todayString get() = "$dayOfMonth$monthOfYear$year"
    val todayFormatted get() = "$dayOfMonth/$monthOfYear/$year"

    val todayInt get() = "$year${withZero(monthOfYear)}${withZero(dayOfMonth)}".toSafeInt()

    val millisNow get() = now.toEpochMilliseconds()

    fun now(): Long {
        return Clock.System.now().toEpochMilliseconds()
    }

    fun formatDate(millis: Long): String {
        val timeZone = TimeZone.currentSystemDefault()
        val time = Instant.fromEpochMilliseconds(millis)
        val dateTime: LocalDateTime = time.toLocalDateTime(timeZone)
        val date = dateTime.date

        return format(date)
    }

    fun second(millis: Long): Long {
        return if (millis > SECOND) millis / SECOND
        else 0
    }

    fun minute(millis: Long): Long {
        return if (millis > MINUTE) millis / MINUTE
        else 0
    }

    fun formatBetterDate(millis: Long): String {
        //need to store timeZone to cloud
        val timeZone = TimeZone.currentSystemDefault()
        val time = Instant.fromEpochMilliseconds(millis)
        val dateTime: LocalDateTime = time.toLocalDateTime(timeZone)
        val date = dateTime.date

        val now = Clock.System.now().toEpochMilliseconds()

        val adjustedTime = dateTime.toInstant(timeZone).toEpochMilliseconds()

        val timeGap = now - adjustedTime

        val timeString: String

        when {
            timeGap < HOUR -> {
                val minute = (timeGap / 1000L / 60L).toInt()
                val minuteString = if (minute < 2) "minute" else "minutes"
                timeString = "$minute  $minuteString ago"
            }
            timeGap < DAY -> {
                val hour = (timeGap / 1000L / 60L / 60L).toInt()
                timeString = if (hour < 2) "an hour ago"
                else "$hour hours ago"
            }
            timeGap < MONTH -> {
                val day = (timeGap / 1000L / 60L / 60L / 24L).toInt()
                timeString = if (day < 2) "a day ago"
                else "$day days ago"
            }
            else -> timeString = format(date)
        }

        return timeString
    }

    private fun format(date: LocalDate): String {
        // pattern = Monday, 12 June , 2020
        return with(date) { "${dayOfWeek.name}, $dayOfMonth ${month.name}, $year".lowercase() }
    }

    fun formatAgo(millis: Long): String {
        val ago = now() - millis
        var stringAgo = "Now"
        when {
            ago > YEAR -> stringAgo = "${ago / YEAR}y"
            ago > MONTH -> stringAgo = "${ago / MONTH}mo"
            ago > DAY -> stringAgo = "${ago / DAY}d"
            ago > HOUR -> stringAgo = "${ago / HOUR}h"
            ago > MINUTE -> stringAgo = "${ago / MINUTE}m"
            ago > SECOND -> stringAgo = "${ago / SECOND}s"
        }
        return stringAgo
    }


    fun formatTime(millis: Long, withSec: Boolean = true): String {
        val sec = if (millis > SECOND) millis / SECOND % 60
        else 0
        val min = if (millis > MINUTE) millis / MINUTE % 60
        else 0
        val hr = if (millis > HOUR) millis / HOUR % 24
        else 0
        val day = if (millis > DAY) millis / DAY % 365
        else 0
        val year = if (millis > YEAR) millis / YEAR
        else 0

        val secString = if (sec > 0) "${sec}sec" else ""
        val minString = if (min > 0) "${min}min" else ""
        val hrString = if (hr > 0) "${hr}hr" else ""
        val dayString = if (day > 0) "${day}day" else ""
        val yearString = if (year > 0) "${year}yr" else ""

        return if (withSec) "$yearString$dayString$hrString$minString$secString"
        else "$yearString$dayString$hrString$minString"
    }

    fun formatTimer(millis: Long): String {
        println("formatting")
        val sec = if (millis > SECOND) millis / SECOND % 60
        else 0
        val min = if (millis > MINUTE) millis / MINUTE % 60
        else 0
        val hr = if (millis > HOUR) millis / HOUR % 24
        else 0
        val day = if (millis > DAY) millis / DAY % 365
        else 0
        val year = if (millis > YEAR) millis / YEAR
        else 0

        Log.i(TAG, "formatTimer: $sec")

        val secString = if (sec > 0) withZero(sec) else "00"
        val minString = if (min > 0) "${withZero(min)}:" else "00:"
        val hrString = if (hr > 0) "${withZero(hr)}:" else ""
        val dayString = if (day > 0) "${withZero(day)}:" else ""
        val yearString = if (year > 0) "${withZero(year)}:" else ""

        val timeString = "$yearString$dayString$hrString$minString$secString"
        Log.i(TAG, "formatTimer: $timeString")

        return timeString
    }

    fun seconds(count: Long): Long {
        return SECOND * count
    }

    fun minutes(count: Long): Long {
        return MINUTE * count
    }

    fun hours(count: Long): Long {
        return HOUR * count
    }

    fun days(count: Long): Long {
        return DAY * count
    }

    fun weeks(count: Long): Long {
        return WEEK * count
    }

    fun months(count: Long): Long {
        return MONTH * count
    }

    fun years(count: Long): Long {
        return YEAR * count
    }

    private fun withZero(num: Long): String {
        return if (num < 10) "0$num"
        else "$num"
    }

    private fun withZero(num : Int): String{
        return if (num < 10) "0$num"
        else "$num"
    }

}