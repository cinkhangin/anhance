@file:Suppress("unused")

package com.naulian.anhance

import android.graphics.Bitmap
import android.net.Uri
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide

//glide extensions
fun loadImageWithGLide(uri: Uri, imageView: ImageView) =
    Glide.with(imageView).load(uri).into(imageView)

fun loadImageWithGLide(url: String, imageView: ImageView) =
    Glide.with(imageView).load(url).into(imageView)

fun loadImageWithGLide(@DrawableRes drawable: Int, imageView: ImageView) =
    Glide.with(imageView).load(drawable).into(imageView)

fun loadImageWithGLide(bitmap: Bitmap, imageView: ImageView) =
    Glide.with(imageView).load(bitmap).into(imageView)

//imageView extensions
fun ImageView.loadWithGlide(uri: Uri) =
    loadImageWithGLide(uri, this)

fun ImageView.loadWithGlide(url: String) =
    loadImageWithGLide(url, this)

fun ImageView.loadWithGlide(@DrawableRes drawable: Int) =
    loadImageWithGLide(drawable, this)

fun ImageView.loadWithGlide(bitmap: Bitmap) =
    loadImageWithGLide(bitmap, this)

