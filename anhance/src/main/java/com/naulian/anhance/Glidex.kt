@file:Suppress("unused")

package com.naulian.anhance

import android.graphics.Bitmap
import android.net.Uri
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit

//glide extensions
fun loadImageWithGLide(
    uri: Uri, imageView: ImageView,
    timeout: Duration = 60.seconds
) = loadWithGlide(uri, imageView, timeout)

fun loadImageWithGLide(
    url: String, imageView: ImageView,
    timeout: Duration = 60.seconds
) = loadWithGlide(url, imageView, timeout)

fun loadImageWithGLide(
    @DrawableRes drawable: Int,
    imageView: ImageView,
    timeout: Duration = 60.seconds
) = loadWithGlide(drawable, imageView, timeout)

fun loadImageWithGLide(
    bitmap: Bitmap, imageView: ImageView,
    timeout: Duration = 60.seconds
) = loadWithGlide(bitmap, imageView, timeout)

private fun <T> loadWithGlide(image: T, imageView: ImageView, timeout: Duration) {
    val duration = timeout.toInt(DurationUnit.MILLISECONDS)
    val option = RequestOptions.timeoutOf(duration) //60 seconds
    Glide.with(imageView)
        .load(image)
        .apply(option)
        .into(imageView)
}

//imageView extensions
fun ImageView.loadWithGlide(
    uri: Uri, timeout: Duration = 60.seconds
) = loadImageWithGLide(uri, this, timeout)

fun ImageView.loadWithGlide(
    url: String, timeout: Duration = 60.seconds
) = loadImageWithGLide(url, this, timeout)

fun ImageView.loadWithGlide(
    @DrawableRes drawable: Int,
    timeout: Duration = 60.seconds
) = loadImageWithGLide(drawable, this, timeout)

fun ImageView.loadWithGlide(
    bitmap: Bitmap, timeout: Duration = 60.seconds
) = loadImageWithGLide(bitmap, this, timeout)

