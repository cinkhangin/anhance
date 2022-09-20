package com.naulian.anhance

import android.util.Log
import kotlinx.datetime.*

@Suppress("unused", "MemberVisibilityCanBePrivate")
object AnDateTime {
    private const val TAG = "AnDateTime"
    //geting a system Clock
    val systemClock get() = Clock.System

    //getting an Instant
    val now get() = systemClock.now()
    val timeZone get() = TimeZone.currentSystemDefault()
    val localDate = systemClock.todayIn(timeZone)
    val localDateTime = now.toLocalDateTime(timeZone)
    val dayOfMonth get() = localDate.dayOfMonth
    val monthOfYear get() = localDate.monthNumber
    val year get() = localDate.year
    val todayString get() = "$dayOfMonth$monthOfYear$year"
    val todayFormatted get() = "$dayOfMonth/$monthOfYear/$year"
    val todayInt get() = "$year${monthOfYear.to0String}${dayOfMonth.to0String}".toSafeInt()
    val millisNow get() = now.toEpochMilliseconds()

    fun formatDate(millis: Long): String {
        val timeZone = TimeZone.currentSystemDefault()
        val time = Instant.fromEpochMilliseconds(millis)
        val dateTime: LocalDateTime = time.toLocalDateTime(timeZone)
        val date = dateTime.date

        return format(date)
    }

    fun formatLongerAgo(millis: Long): String {
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
        val sec = millis.leftSecond
        val min = millis.leftMinute
        val hr = millis.leftHour
        val day = millis.leftDay
        val year = millis.leftYear

        val secString = sec.toClockString
        val minString = min.toClockString
        val hrString = hr.toClockString
        val dayString = day.toDayString
        val yearString = year.toYearString

        val rawTimeString = "$yearString$dayString$hrString:$minString:$secString"

        val timeString = rawTimeString.removePrefix(" 00:")
            .removePrefix("00:")
        Log.i(TAG, "formatTimer: $timeString")

        return timeString
    }
}