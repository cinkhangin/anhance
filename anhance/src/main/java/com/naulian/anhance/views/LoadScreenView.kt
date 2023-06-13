@file:Suppress("unused")

package com.naulian.anhance.views

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.animation.doOnEnd
import androidx.core.view.isVisible
import androidx.core.widget.ContentLoadingProgressBar
import com.naulian.anhance.R
import com.naulian.anhance.dp

class LoadScreenView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    LinearLayout(context, attrs) {

    private val linearRoot: LinearLayout
    private val textView: TextView
    private val progressBar: ContentLoadingProgressBar

    init {
        LayoutInflater.from(context).inflate(R.layout.view_load_screen, this, true)

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.LoadScreenView)
        val text = typedArray.getString(R.styleable.LoadScreenView_android_text)
        val textColor = typedArray.getColor(
            R.styleable.LoadScreenView_android_textColor, Color.BLACK
        )
        val textSize = typedArray.getDimension(R.styleable.LoadScreenView_android_textSize, 14.dp)
        val progressColor = typedArray.getColor(
            R.styleable.LoadScreenView_progressColor, Color.BLACK
        )
        typedArray.recycle()

        linearRoot = findViewById(R.id.linear)
        textView = findViewById(R.id.textInfo)
        progressBar = findViewById(R.id.progress)

        progressBar.indeterminateTintList = ColorStateList.valueOf(progressColor)
        textView.textSize = textSize
        textView.setTextColor(textColor)
        textView.text = text.toString().ifEmpty { "Please wait..." }

        if (!isInEditMode) {
            linearRoot.scaleX = 0f
            linearRoot.scaleY = 0f
            isVisible = false
        }
    }

    var textColor
        get() : Int = textView.currentTextColor
        set(value) {
            textView.setTextColor(value)
        }

    var text
        get() : CharSequence = textView.text
        set(value) {
            textView.text = value
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