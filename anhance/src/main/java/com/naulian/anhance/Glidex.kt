@file:Suppress("unused")

package com.naulian.anhance

import android.graphics.Bitmap
import android.net.Uri
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

//glide extensions
fun loadImageWithGLide(uri: Uri, imageView: ImageView) =
    loadWithGlide(uri, imageView)

fun loadImageWithGLide(url: String, imageView: ImageView) =
    loadWithGlide(url, imageView)

fun loadImageWithGLide(@DrawableRes drawable: Int, imageView: ImageView) =
    loadWithGlide(drawable, imageView)

fun loadImageWithGLide(bitmap: Bitmap, imageView: ImageView) =
    loadWithGlide(bitmap, imageView)

private fun <T> loadWithGlide(image: T, imageView: ImageView) {
    val option = RequestOptions.timeoutOf(1000 * 60)
    Glide.with(imageView)
        .load(image)
        .apply(option)
        .into(imageView)
}

//imageView extensions
fun ImageView.loadWithGlide(uri: Uri) =
    loadImageWithGLide(uri, this)

fun ImageView.loadWithGlide(url: String) =
    loadImageWithGLide(url, this)

fun ImageView.loadWithGlide(@DrawableRes drawable: Int) =
    loadImageWithGLide(drawable, this)

fun ImageView.loadWithGlide(bitmap: Bitmap) =
    loadImageWithGLide(bitmap, this)

