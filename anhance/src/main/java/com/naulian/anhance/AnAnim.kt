@file:Suppress("unused")

package com.naulian.anhance

import android.animation.ValueAnimator
import android.widget.TextView
import androidx.core.animation.doOnEnd

fun animateType(textView: TextView, text: String, onEnd: () -> Unit) {
    animateWrite(textView, text) {
        animateDelete(textView, text) { onEnd() }
    }
}

fun animateWrite(textView: TextView, text: String, onEnd: () -> Unit) {
    val animator = ValueAnimator.ofInt(0, text.length)
    animator.duration = secondOf(1)
    animator.addUpdateListener {
        val progress = it.animatedValue as Int
        textView.text = text.substring(0, progress)
    }
    animator.doOnEnd { onEnd() }
    animator.start()
}

fun animateDelete(textView: TextView, text: String, onEnd: () -> Unit) {
    val animator = ValueAnimator.ofInt(0, text.length)
    animator.duration = secondOf(1)
    animator.addUpdateListener {
        val progress = it.animatedValue as Int
        val until = text.length - progress
        textView.text = text.substring(0, until)
    }
    animator.doOnEnd { onEnd() }
    animator.start()
}