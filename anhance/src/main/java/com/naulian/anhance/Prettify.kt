@file:Suppress("unused")

package com.naulian.anhance

import android.text.SpannableString
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import androidx.core.text.HtmlCompat

private const val HTML_MODE_COMPACT = HtmlCompat.FROM_HTML_MODE_COMPACT
fun String.size(dp : Int) : SpannableString {
    val span = SpannableString(this)
    val flag = SpannableString.SPAN_INCLUSIVE_INCLUSIVE
    span.setSpan(AbsoluteSizeSpan(dp.toPx().toInt()), 0, this.length, flag)
    return span
}
fun String.html() = HtmlCompat.fromHtml(this, HTML_MODE_COMPACT)
fun htmlSpanned(string: String) = HtmlCompat.fromHtml(string, HTML_MODE_COMPACT)
fun String.color(colorHex: String) = "<font color=$colorHex>$this</font>"
fun String.colorHtml(colorHex : String) : Spanned {
    return this.color(colorHex).html()
}
