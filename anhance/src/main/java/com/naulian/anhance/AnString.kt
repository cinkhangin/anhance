package com.naulian.anhance

import androidx.core.text.isDigitsOnly

fun String.toSafeInt() = if(this.isDigitsOnly()) this.toInt() else 0