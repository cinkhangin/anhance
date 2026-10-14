package com.ckgin.anhance

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Locale
import java.util.TimeZone

class TimeTest {
    @Test
    fun unitsAreConsistent() {
        assertEquals(1000L, SECOND)
        assertEquals(60 * SECOND, MINUTE)
        assertEquals(60 * MINUTE, HOUR)
        assertEquals(24 * HOUR, DAY)
        assertEquals(7 * DAY, WEEK)
        assertEquals(30 * DAY, MONTH)
        assertEquals(365 * DAY, YEAR)
    }

    @Test
    fun intAndLongConversionsAgree() {
        val ints = listOf(3.seconds, 3.minutes, 3.hours, 3.days, 3.weeks, 3.months, 3.years)
        val longs = listOf(3L.seconds, 3L.minutes, 3L.hours, 3L.days, 3L.weeks, 3L.months, 3L.years)
        assertEquals(ints, longs)
        assertEquals(ints, listOf(secondOf(3), minuteOf(3), hourOf(3), dayOf(3), weekOf(3), monthOf(3), yearOf(3)))
        assertEquals(
            longs,
            listOf(secondOf(3L), minuteOf(3L), hourOf(3L), dayOf(3L), weekOf(3L), monthOf(3L), yearOf(3L))
        )
        assertEquals(0L, 0.seconds)
        assertEquals(-SECOND, (-1).seconds)
        assertEquals(2_147_483_647_000L, Int.MAX_VALUE.seconds)
    }

    @Test
    fun countsTruncateToWholeUnits() {
        assertEquals(0L, 999L.secondCount)
        assertEquals(61L, (MINUTE + SECOND).secondCount)
        assertEquals(1L, (MINUTE + SECOND).minuteCount)
        assertEquals(2L, (2 * HOUR).hourCount)
        assertEquals(3L, (3 * DAY).dayCount)
        assertEquals(4L, (4 * WEEK).weekCount)
        assertEquals(5L, (5 * MONTH).monthCount)
        assertEquals(6L, (6 * YEAR).yearCount)
        assertEquals(-1L, (-SECOND).secondCount)
    }

    @Test
    fun durationRemaindersExcludeLargerUnits() {
        val duration = YEAR + 2 * DAY + 3 * HOUR + 4 * MINUTE + 5 * SECOND
        assertEquals(1L, duration.leftYear)
        assertEquals(2L, duration.leftDay)
        assertEquals(3L, duration.leftHour)
        assertEquals(4L, duration.leftMinute)
        assertEquals(5L, duration.leftSecond)
    }

    @Test
    fun timerIncludesHoursDaysAndYearsOnlyWhenNeeded() {
        assertEquals("00:00", 0L.formatTimer())
        assertEquals("00:03", 3.seconds.formatTimer())
        assertEquals("01:16", (MINUTE + 16 * SECOND).formatTimer())
        assertEquals("01:00:00", HOUR.formatTimer())
        assertEquals("4d 01:35:00", (4 * DAY + HOUR + 35 * MINUTE).formatTimer())
        assertEquals("1y 00:00:00", YEAR.formatTimer())
    }

    @Test
    fun clockContainsAllComponents() {
        assertEquals("00:00:00", 0L.formatClock())
        assertEquals("01:02:03", (HOUR + 2 * MINUTE + 3 * SECOND).formatClock())
        assertEquals("1y 2d 03:04:05", (YEAR + 2 * DAY + 3 * HOUR + 4 * MINUTE + 5 * SECOND).formatClock())
    }

    @Test
    fun durationFormatsOmitZeroUnits() {
        assertEquals("", 0L.formatDuration())
        assertEquals("3s", 3.seconds.formatDuration())
        assertEquals("1y 2d 3hr 4m 5s", (YEAR + 2 * DAY + 3 * HOUR + 4 * MINUTE + 5 * SECOND).formatDuration())
        assertEquals("1h2m3s", (HOUR + 2 * MINUTE + 3 * SECOND).formatLeft())
        assertEquals("1h2m", (HOUR + 2 * MINUTE + 3 * SECOND).formatLeft(withSec = false))
    }

    @Test
    fun clockPaddingDoesNotCorruptNegativeNumbers() {
        assertEquals("00", 0.to0String)
        assertEquals("09", 9L.to0String)
        assertEquals("10", 10.to0String)
        assertEquals("-1", (-1).to0String)
        assertEquals("-1", (-1L).to0String)
    }

    @Test
    fun timestampsUseRequestedPatternAndLocale() {
        val original = TimeZone.getDefault()
        try {
            TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
            assertEquals("1970-01-01 Thursday", 0L.formatWith("yyyy-MM-dd EEEE", Locale.US))
            assertEquals("janvier", 0L.formatWith("MMMM", Locale.FRANCE))
        } finally {
            TimeZone.setDefault(original)
        }
    }

    @Test
    fun relativeTimesHandlePastAndFuture() {
        assertEquals(0L, 0L.timeLeft)
        assertEquals(false, 0L.isTimeLeft)
        val before = millisOfNow
        val future = before + DAY
        assertTrue(future.timeLeft in 1..DAY)
        assertTrue(future.isTimeLeft)
        assertTrue(before.timeGap >= 0)
        assertEquals("Now", future.formatAgo())
        assertEquals("2s", (millisOfNow - 2 * SECOND).formatAgo())
        assertEquals("2m", (millisOfNow - 2 * MINUTE).formatAgo())
        assertEquals("2h", (millisOfNow - 2 * HOUR).formatAgo())
        assertEquals("2d", (millisOfNow - 2 * DAY).formatAgo())
        assertEquals("2mo", (millisOfNow - 2 * MONTH).formatAgo())
        assertEquals("2y", (millisOfNow - 2 * YEAR).formatAgo())
        assertTrue(last(DAY) in (before - DAY)..(millisOfNow - DAY))
    }
}
