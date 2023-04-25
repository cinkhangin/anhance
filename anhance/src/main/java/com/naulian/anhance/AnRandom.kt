@file:Suppress("unused")

package com.naulian.anhance

import kotlin.random.Random

val Int.random get() = AnRandom.generateInt(0, this)
val Long.random get() = AnRandom.generateLong(0, this)

fun randomOf(from: Int = 0, until: Int) = AnRandom.generateInt(from, until)
fun randomOf(from: Long = 0L, until: Long) = AnRandom.generateLong(from, until)
fun randomFloat() = AnRandom.generateFloat()

val uniqueSeed get() = AnRandom.generator()

object AnRandom {
    val generator get() = generator()

    fun generateFloat() = generator.nextFloat()

    fun generateInt(from: Int, until: Int) = generator.nextInt(from, until)
    fun generateLong(from: Long, until: Long) = generator.nextLong(from, until)


    //private methods==================
    fun generator(): Random {
        val runtimeRnd = Random.nextInt(1, 1000)
        val seed = millisOfNow + runtimeRnd
        return Random(seed)
    }
}