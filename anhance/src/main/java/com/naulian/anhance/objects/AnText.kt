package com.naulian.anhance.objects

import com.naulian.anhance.random

object AnText {
    private const val LOWERCASES = "abcdefghijklmnopqrstuvwxyz"
    private const val UPPERCASES = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    private const val DECIMALS = "0123456789"

    private val stars = hashMapOf(
        3 to "***",
        4 to "****",
        5 to "*****",
        6 to "******",
        7 to "*******",
        8 to "********",
        9 to "*********",
        10 to "**********",
    )
    private val words = arrayListOf(
        "fuck",
        "sex",
        "porn"
    )

    fun censor(text: String): String {
        var output = text
        words.forEach { word ->
            if (text.contains(word, true)) {
                val star = stars[word.length] ?: "***"
                output = output.replace(word, star)
            }
        }
        return output
    }

    fun generateRandomString(example: String): String {
        if (example.isEmpty()) return "ooh!oh!"

        var raw = ""
        var output = ""
        example.forEach {
            if (LOWERCASES.contains(it, false)){
                raw += LOWERCASES
            }
            if (UPPERCASES.contains(it, false)) raw += UPPERCASES
            if (DECIMALS.contains(it, false)) raw += DECIMALS
        }

        repeat(example.length) {
            val rndIndex = raw.length.random
            output += raw.elementAt(rndIndex)
        }

        return output
    }

}