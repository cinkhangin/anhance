@file:Suppress("unused")

package com.naulian.anhance

import android.graphics.Color
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.card.MaterialCardView
import kotlin.math.abs

fun View.onClick(action: (View) -> Unit) {
    setOnClickListener { action(it) }
}

fun View.doOnClick(action: (View) -> Unit) {
    setOnClickListener { action(it) }
}

fun View.onLongClick(action: (View) -> Unit) {
    setOnLongClickListener { action(it); true }
}

fun View.doOnLongClick(action: (View) -> Unit) {
    setOnLongClickListener { action(it); true }
}

fun EditText.textString() = text.toString()
fun EditText.textTrim() = textString().trim()

fun MaterialToolbar.onNavClick(action: () -> Unit) {
    setNavigationOnClickListener { action() }
}

@Suppress("UNUSED_ANONYMOUS_PARAMETER")
fun RecyclerView.onLayoutChanged(action: (Boolean) -> Unit) {
    addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
        action(oldBottom < bottom)
    }
}

fun ViewPager2.setUpScaleYPageTransformer() {
    clipToPadding = false
    clipChildren = false
    offscreenPageLimit = 3

    val compositePageTransformer = CompositePageTransformer()
    compositePageTransformer.addTransformer(MarginPageTransformer(24))
    compositePageTransformer.addTransformer { page, position ->
        val r = 1 - abs(position)
        page.scaleY = 0.85f + r * 0.15f
    }
    setPageTransformer(compositePageTransformer)
}

fun ViewPager2.onPageSelected(action: (Int) -> Unit) {
    registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            action(position)
        }
    })
}

fun TextView.setTextColorRes(@ColorRes resId: Int) {
    setTextColor(context.getColor(resId))
}

fun MaterialCardView.setCardBgColorRes(@ColorRes resId: Int) {
    setCardBackgroundColor(context.getColor(resId))
}

fun CardView.setCardBgHex(hexString: String) {
    val color = Color.parseColor(hexString)
    setCardBackgroundColor(color)
}

fun View.setBgColorHex(hexColor: String) {
    setBackgroundColor(hexColor.toColorInt())
}

fun String.toColorInt(): Int {
    if (!startsWith("#")) {
        logError("toColorInt", "Error : '$this' is not a hex string")
        return Color.WHITE
    }

    return when (length) {
        4, 7, 9 -> Color.parseColor(this)
        else -> {
            logError("toColorInt", "Error : '$this' is not a hex string")
            Color.WHITE
        }
    }
}
