@file:Suppress("unused")

package com.naulian.anhance

import android.content.ClipData
import android.content.Context
import android.os.Build
import androidx.core.text.isDigitsOnly

fun String.toSafeInt() = if (this.isDigitsOnly()) this.toInt() else 0
fun trim(string: String) = string.trim()
fun String.caseContains(string: CharSequence) =
    contains(string, false)

fun String.caseContains(char: Char) =
    contains(char, false)

fun String.copy(context: Context) {
    AnString.copy(context, this)
}

fun Context.copyString(string: String) {
    AnString.copy(this, string)
}

fun String.isValidEmail(): Boolean {
    val emailRegex = "^[A-Za-z0-9+_.-]+@(.+)\$".toRegex()
    return matches(emailRegex)
}

fun String.censor() = AnString.censor(this)
fun String.generateMore() = AnString.generateRandomString(this)
fun String.injectLast(string: String) = AnString.addMoreLast(string, this)
fun String.injectLast(number: Number) = AnString.addMoreLast(number.toString(), this)
fun String.inject(string: String) = AnString.addMoreFirst(string, this)
fun String.inject(number: Number) = AnString.addMoreFirst(number.toString(), this)

//to avoid SetTextI18n warning
fun str(string: String) = string.trim()

const val LOWERS = "abcdefghijklmnopqrstuvwxyz"
const val UPPERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
const val DECIMALS = "0123456789"
const val SYMBOLS = "~`!@#\$%^&*()_-+={[}]|\\:;\"'<,>.?/"

object AnString {
    private val words = arrayListOf(
        "fuck",
        "sex",
        "porn"
    )

    fun censor(text: String): String {
        var output = text
        words.forEach { word ->
            if (word in text) {
                val star = buildString {
                    repeat(word.length) {
                        append("*")
                    }
                }
                output = output.replace(word, star)
            }
        }
        return output
    }

    fun generateRandomString(example: String): String {
        if (example.isEmpty()) return "ooh!oh!"

        val source = loopForString(example) { char ->
            when (char) {
                in LOWERS -> LOWERS
                in UPPERS -> UPPERS
                in DECIMALS -> DECIMALS
                in SYMBOLS -> SYMBOLS
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

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2) {
            val message = if (string.length > 20) "Copied" else string
            context.showToast(message)
        }
    }
}

inline fun loopForString(times: Int, block: (Int) -> String) = buildString {
    repeat(times) {
        append(block(it))
    }
}

inline fun loopForString(range: IntRange, block: (Int) -> String) = buildString {
    for (i in range) {
        append(block(i))
    }
}

inline fun <T> loopForString(list: ArrayList<T>, block: (T) -> String) = buildString {
    for (element in list) {
        append(block(element))
    }
}

inline fun <T> loopForString(iterator: Iterator<T>, block: (T) -> String) = buildString {
    while (iterator.hasNext()) {
        append(block(iterator.next()))
    }
}

inline fun loopForString(string: String, block: (Char) -> String) = buildString {
    for (char in string) {
        append(block(char))
    }
}
