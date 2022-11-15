@file:Suppress("unused")

package com.naulian.anhance

import android.content.ClipData
import android.content.Context
import androidx.core.text.isDigitsOnly

fun String.toSafeInt() = if (this.isDigitsOnly()) this.toInt() else 0
fun trim(string: String) = string.trim()
fun String.caseContains(string: CharSequence) =
    contains(string, false)

fun String.caseContains(char: Char) =
    contains(char, false)

fun String.copy(context: Context) {
   AnString.copy(context ,this)
}

fun Context.copyString(string: String) {
    AnString.copy(this, string)
}

fun String.censor() = AnString.censor(this)
fun String.generateMore() = AnString.generateRandomString(this)
fun String.injectLast(string: String) = AnString.addMoreLast(string, this)
fun String.injectLast(number: Number) = AnString.addMoreLast(number.toString(), this)
fun String.inject(string: String) = AnString.addMoreFirst(string, this)
fun String.inject(number: Number) = AnString.addMoreFirst(number.toString(), this)

object AnString {
    private const val lowerCases = "abcdefghijklmnopqrstuvwxyz"
    private const val upperCases = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    private const val decimals = "0123456789"
    private const val symbols = "~`!@#\$%^&*()_-+={[}]|\\:;\"'<,>.?/"

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
            if (word in text) {
                val star = stars[word.length] ?: "***"
                output = output.replace(word, star)
            }
        }
        return output
    }

    fun generateRandomString(example: String): String {
        if (example.isEmpty()) return "ooh!oh!"

        val source = loopForString(example) { char ->
            when (char) {
                in lowerCases -> lowerCases
                in upperCases -> upperCases
                in decimals -> decimals
                in symbols -> symbols
                else -> ""
            }
        }

        val output = loopForString(example.length) {
            val rndIndex = source.length.random
            source.elementAt(rndIndex).toString()
        }

        return trim(output)
    }

    fun addMoreLast(string: String, placeholder: String): String {
        if (string.length >= placeholder.length) return string
        val prefix = placeholder.dropLast(string.length)
        return "$prefix$string"
    }

    fun addMoreFirst(string: String, placeholder: String): String {
        if (string.length >= placeholder.length) return string
        val suffix = placeholder.drop(string.length)
        return "$string$suffix"
    }

    fun copy(context: Context, string: String) {
        val data = ClipData.newPlainText("clip", string)
        context.clipboardManager.setPrimaryClip(data)
        context.showToast(string)
    }
}
