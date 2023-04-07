@file:Suppress("unused")

package com.naulian.anhance

import android.animation.ValueAnimator
import android.view.animation.LinearInterpolator
import android.widget.TextView
import androidx.core.animation.doOnEnd

fun animateType(
    textView: TextView, duration: Long = 1000,
    text: String, onEnd: () -> Unit
) = animateWrite(textView, text, duration) {
    animateDelete(textView, text, duration, onEnd)
}


fun animateWrite(
    textView: TextView, text: String,
    duration: Long = 1000, onEnd: () -> Unit
) {
    val animator = ValueAnimator.ofInt(0, text.length)
    animator.duration = duration
    animator.interpolator = LinearInterpolator()
    animator.addUpdateListener {
        val progress = it.animatedValue as Int
        textView.text = text.substring(0, progress)
    }
    animator.doOnEnd { onEnd() }
    animator.start()
}

fun animateDelete(
    textView: TextView, text: String,
    duration: Long = 1000, onEnd: () -> Unit
) {
    val animator = ValueAnimator.ofInt(0, text.length)
    animator.duration = duration
    animator.interpolator = LinearInterpolator()
    animator.addUpdateListener {
        val progress = it.animatedValue as Int
        val until = text.length - progress
        textView.text = text.substring(0, until)
    }
    animator.doOnEnd { onEnd() }
    animator.start()
}