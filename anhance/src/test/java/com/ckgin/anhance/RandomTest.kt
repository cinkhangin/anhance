package com.ckgin.anhance

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class RandomTest {
    @Test
    fun randomNumbersStayWithinExclusiveBounds() {
        repeat(1000) {
            assertTrue(randomOf(-5, 7) in -5 until 7)
            assertTrue(randomOf(-5L, 7L) in -5L until 7L)
            assertTrue(10.random in 0 until 10)
            assertTrue(10L.random in 0L until 10L)
            assertTrue(randomFloat() >= 0f)
            assertTrue(randomFloat() < 1f)
        }
    }

    @Test
    fun singleElementRangesAlwaysReturnTheirElement() {
        assertEquals(4, randomOf(4, 5))
        assertEquals(4L, randomOf(4L, 5L))
    }

    @Test
    fun invalidBoundsThrowClearly() {
        org.junit.Assert.assertThrows(IllegalArgumentException::class.java) { 0.random }
        org.junit.Assert.assertThrows(IllegalArgumentException::class.java) { (-1L).random }
        org.junit.Assert.assertThrows(IllegalArgumentException::class.java) { randomOf(3, 3) }
        org.junit.Assert.assertThrows(IllegalArgumentException::class.java) { randomOf(4L, 3L) }
    }
}
