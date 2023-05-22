package com.naulian.anhance

val Long.safeToDivide get() = if (this > 0L) this else 1L
infix fun Long.safeDiv(divider: Long) =  this.safeToDivide / divider