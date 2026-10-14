package com.ckgin.anhance

val Long.safeDivider get() = if (this == 0L) 1L else this
infix fun Long.safeDiv(divider: Long) = this / divider.safeDivider
