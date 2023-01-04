@file:Suppress("unused")

package com.naulian.anhance

import android.view.View
import android.widget.EditText
import com.google.android.material.appbar.MaterialToolbar

fun View.onClick(action : (view : View) -> Unit){
    setOnClickListener { action(it) }
}

fun View.onLongClick(action: (view: View) -> Unit){
    setOnLongClickListener { action(it); true }
}

fun EditText.textString() = text.toString()
fun EditText.textTrim() = textString().trim()

fun MaterialToolbar.onNavClick(action : () -> Unit){
    setNavigationOnClickListener { action() }
}