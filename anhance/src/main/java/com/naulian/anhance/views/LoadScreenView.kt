@file:Suppress("unused")

package com.naulian.anhance.views

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.animation.doOnEnd
import androidx.core.view.isVisible
import com.naulian.anhance.R
import com.naulian.anhance.str

class LoadScreenView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    LinearLayout(context, attrs) {

    private val linearRoot: LinearLayout
    private val textView: TextView
    private val progressBar: ProgressBar

    init {
        LayoutInflater.from(context).inflate(R.layout.view_load_screen, this, true)

        linearRoot = findViewById(R.id.linear)
        textView = findViewById(R.id.textInfo)
        textView.text = str("Please wait...")
        progressBar = findViewById(R.id.progress)

        if (!isInEditMode) {
            linearRoot.scaleX = 0f
            linearRoot.scaleY = 0f
            isVisible = false
        }

    }

    fun setBgHex(colorHex: String) {
        val color = Color.parseColor(colorHex)
        linearRoot.setBackgroundColor(color)
    }

    fun setBgColor(color: Int) {
        linearRoot.setBackgroundColor(color)
    }

    fun setBgColorRes(@ColorRes resId: Int) {
        val color = context.getColor(resId)
        linearRoot.setBackgroundColor(color)
    }

    fun changeText(message: String) {
        textView.text = message
    }

    fun show(animation: AnimatorSet = showAnimation) {
        isVisible = true
        animation.start()
    }

    fun hide(animation: AnimatorSet = hideAnimation) {
        animation.start()
        animation.doOnEnd { isVisible = false }
    }

    private val showAnimation
        get() : AnimatorSet {
            val animatorSet = AnimatorSet()
            val scaleX = ObjectAnimator.ofFloat(linearRoot, "scaleX", 0f, 1f)
            val scaleY = ObjectAnimator.ofFloat(linearRoot, "scaleY", 0f, 1f)
            animatorSet.playTogether(scaleX, scaleY)
            animatorSet.interpolator = AccelerateDecelerateInterpolator()
            animatorSet.duration = 300
            return animatorSet
        }

    private val hideAnimation
        get() : AnimatorSet {
            val animatorSet = AnimatorSet()
            val scaleX = ObjectAnimator.ofFloat(linearRoot, "scaleX", 1f, 0f)
            val scaleY = ObjectAnimator.ofFloat(linearRoot, "scaleY", 1f, 0f)
            animatorSet.playTogether(scaleX, scaleY)
            animatorSet.interpolator = AccelerateDecelerateInterpolator()
            animatorSet.duration = 300
            return animatorSet
        }
}