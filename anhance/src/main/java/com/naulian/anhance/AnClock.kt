package com.naulian.anhance

val Int.sec get() = AnDateTime.seconds(this)
val Int.min get() = AnDateTime.minutes(this)
val Int.hr get() = AnDateTime.hours(this)
val Int.day get() = AnDateTime.days(this)
val Int.week get() = AnDateTime.weeks(this)
val Int.month get() = AnDateTime.months(this)
val Int.year get() = AnDateTime.years(this)

val Long.sec get() = AnDateTime.seconds(this)
val Long.min get() = AnDateTime.minutes(this)
val Long.hr get() = AnDateTime.hours(this)
val Long.day get() = AnDateTime.days(this)
val Long.week get() = AnDateTime.weeks(this)
val Long.month get() = AnDateTime.months(this)
val Long.year get() = AnDateTime.years(this)
