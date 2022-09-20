package com.naulian.anhance

import org.junit.Test

import org.junit.Assert.*
import kotlin.time.Duration.Companion.seconds

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun test(){
        assertEquals(3 , 3001L.toSecond)
        assertEquals(0 , 0L.toSecond)
    }

}