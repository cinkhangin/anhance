package com.ckgin.anhance

import org.junit.Assert.assertEquals
import org.junit.Test

class MathTest {
    @Test
    fun divisionPreservesZeroAndNegativeNumerators() {
        assertEquals(0L, 0L safeDiv 1L)
        assertEquals(-2L, -6L safeDiv 3L)
        assertEquals(-2L, 6L safeDiv -3L)
        assertEquals(2L, 7L safeDiv 3L)
    }

    @Test
    fun zeroDivisorFallsBackToOne() {
        assertEquals(6L, 6L safeDiv 0L)
        assertEquals(-6L, -6L safeDiv 0L)
        assertEquals(0L, 0L safeDiv 0L)
        assertEquals(1L, 0L.safeDivider)
        assertEquals(-3L, (-3L).safeDivider)
    }

    @Test
    fun castingAndRoundingFollowKotlinSemantics() {
        assertEquals(2, intOf(2.9f))
        assertEquals(-2, intOf(-2.9f))
        assertEquals(2, intOf(2L))
        assertEquals(2L, longOf(2))
        assertEquals(2f, floatOf(2))
        assertEquals(3, roundIntOf(2.5f))
        assertEquals(-2L, roundLongOf(-2.5f))
    }
}
