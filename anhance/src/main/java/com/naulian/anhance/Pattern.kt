@file:Suppress("unused")

package com.naulian.anhance

object Pattern {
    /*
    "G" //AD
    "y" //1996, 96
    "Y" //2009, 09 week year is defines as year's first Thursday:
    "M" //July, Jul, 07 context sensitive
    "L" //July, Jul, 07 standalone form
    "w" //27
    "W" //2
    "D" //189
    "d" //10
    "F" //2
    "E" //Tuesday, Tue
    "u" //1 = Monday, ..., 7 = Sunday)
    "a" //PM
    "H" //0-23
    "k" //1-24
    "K" //0-11
    "h" //1-12
    "m" //minutes
    "s" //seconds
    "S"
    "z" //Pacific Standard Time; PST; GMT-08:00 General time zone
    "Z" //-0800 RFC 822 time zone
    "X" //-08; -0800; -08:00 ISO 8601 time zone*/

    const val SPACE = " "

    const val WEEKDAY1 = "E"
    const val WEEKDAY3 = "EEE"
    const val WEEKDAY_FULL = "EEEE"

    const val PMAM = "a"
    const val AMPM = "a"

    const val HOUR24 = "H"
    const val HOUR24_2 = "HH"
    const val HOUR12 = "h"
    const val HOUR12_2 = "hh"

    const val MINUTE = "m"
    const val MINUTE_2 = "mm"

    const val SECOND = "s"
    const val SECOND_2 = "ss"

    const val ADBC = "G"
    const val YEAR2 = "yy"
    const val YEAR_FULL = "yyyy"

    const val MONTH2 = "MM"
    const val MONTH3 = "MMM"
    const val MONTH_FULL = "MMMM"

    const val DAY2 = "dd"

    fun generatePattern(vararg symbol: String): String {
        return symbol.joinToString("")
    }
}

fun main() {
    val pattern = Pattern.generatePattern(
        Pattern.YEAR_FULL,
        Pattern.MONTH_FULL,
        Pattern.DAY2,
        Pattern.SPACE,
        Pattern.WEEKDAY_FULL
    )

    println(pattern)
}

