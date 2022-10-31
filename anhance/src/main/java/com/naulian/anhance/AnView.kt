package com.naulian.anhance

import android.view.View

fun View.onClick(action : (view : View) -> Unit){
    setOnClickListener { action(it) }
}

fun View.onLongClick(action: (view: View) -> Unit){
    setOnLongClickListener { action(it); true }
}