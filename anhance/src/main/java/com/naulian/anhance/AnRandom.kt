package com.naulian.anhance

import kotlin.random.Random

val Int.random get() = AnRandom.generateInt(0, this)
val Long.random get() = AnRandom.generateLong(0 , this)

object AnRandom {
    val generator get() = generator()

    fun generateInt(from: Int, until: Int): Int {
        return generator.nextInt(from, until)
    }

    fun generateLong(from: Long, until: Long): Long {
        return generator.nextLong(from, until)
    }

    //private methods==================
    private fun generator(): Random {
        val runtimeRnd = Random.nextInt(1, 1000)
        val seed = millisOfNow + runtimeRnd
        return Random(seed)
    }
}