@file:Suppress("unused")

package com.naulian.anhance

import androidx.core.text.HtmlCompat

private const val keywords =
    "package import val var fun this override super private class interface enum object constructor null return init true false const companion lateinit data when else"
private const val lists = "arrayListOf( sortedMapOf( hashMapOf( listOf("

fun String.wrap(flag: String) =
    when (flag) {
        "c" -> "//$this"
        "e" -> "@num$this"
        "i" -> "t@b\"$this\","
        "s" -> "\"$this\""
        "t" -> "t@b$this"
        else -> this
    }

fun String.unWrap(flag: String) =
    when (flag) {
        "c" -> this.replace("//", "")
        "e" -> this.replace("@num", "")
        "i" -> this.replace("\"", "").replace("t@b", "").replace(",", "")
        "s" -> this.replace("\"", "")
        "t" -> this.replace("t@b", "")
        else -> this
    }

fun String.wrapPrettify(flag: String) = this.wrap(flag).prettify()

fun String.color(color: String) = "<font color=$color>$this</font>"

fun String.string() = this.color("#54B33E")
fun String.comment() = this.color("#7EC3E6")
fun String.method() = this.color("#FFC66D")
fun String.digit() = this.color("#00AAFF")
fun String.property() = this.color("#ED94FF")

fun String.keyword() = this.color("#ED864A")

fun String.italic() = "<i>$this</i>"
fun String.bold() = "<b>$this</b>"

fun String.html() = HtmlCompat.fromHtml(this, HtmlCompat.FROM_HTML_MODE_COMPACT)

fun String.indent(times: Int = 1): String {
    var tabs = ""
    repeat(times) {
        tabs += "&nbsp;&nbsp;&nbsp;&nbsp"
    }
    return "$tabs$this"
}

fun String.prettifyHtml() = this.prettify().html()

fun String.smaller(times: Int = 1): String {
    var output = this
    repeat(times) {
        output = "<small>$output</small>"
    }
    return output
}

fun String.bigger(times: Int = 1): String {
    var output = this
    repeat(times) {
        output = "<big>$output</big>"
    }
    return output
}


fun String.prettify(): String {
    var input = this
    var output = ""
    var isString = false

    if (this.contains("t@b")) {
        input = this.replace("t@b", "")
        output = output.indent()
    }

    if (this.contains("@num")) {
        val enum = this.replace("@num", "")
        val comma = ",".keyword()
        return "${enum.property()}$comma".indent()
    }

    if (input.trim().startsWith("//")) {
        return input.comment()
    }

    if (input.contains("(")) {
        input = input.replace("(", "( ")
    }

    if (input.contains(")")) {
        input = input.replace(")", " )")
    }

    val tokens = input.split(" ")

    for (token in tokens) {
        var pretty = ""

        if (token.contains(".")) {
            output += prettifyMethod(token)
            continue
        }

        if (token.contains("(")) {

            if (lists.contains(token)) {
                pretty = token.italic()
                pretty = pretty.replace("(", "")
                pretty += "("
                output += "$pretty "
                continue
            }

            val end = token.indexOf("(")
            val sub = token.substring(0, end)
            pretty = token.replace(sub, sub.method())
            output += "$pretty "
            continue
        }

        if (token.contains("\"") || isString) {
            pretty += token.string()

            if (pretty.contains("\",")) {
                pretty = pretty.replace(",", "")
                pretty += ",".keyword()
            }

            output += "$pretty "
            isString = !isString

            if (token.last().toString() == "\"") {
                isString = false
            }

            continue
        }

        if (keywords.contains(token)) {
            pretty = token.keyword()
            output += "$pretty "
            continue
        }

        if (lists.contains(token)) {
            pretty = token.italic()

            if (pretty.contains("(")) {
                pretty = pretty.replace("(", "")
                pretty += "("
            }
            output += "$pretty "
            continue
        }

        output += "$token "
    }
    output = output.replace("( ", "(")
        .replace(" )", ")")
    return output
}

fun prettifyMethod(token: String): String {
    val start = token.indexOf(".")
    var end = token.length
    var isMethod = false

    if (token.contains("(")) {
        end = token.indexOf("(")
        isMethod = true
    }

    if (token.contains(")")) {
        end = token.indexOf(")")
    }

    val sub = token.substring(start, end)
    val pretty = if (isMethod) token.replace(sub, sub.method())
    else token.replace(sub, sub.property())
    return "$pretty "
}