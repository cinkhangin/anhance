package com.naulian.anhance

import kotlin.random.Random

val Int.random get() = Random.nextInt(this)
val Long.random get() = Random.nextLong(this)