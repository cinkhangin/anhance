package com.ckgin.anhance

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class StringTest {
    @Test
    fun safeIntAcceptsSignedNumbersAndNeverThrowsForInvalidInput() {
        mapOf("123" to 123, "-42" to -42, "+7" to 7, "0009" to 9,
            "2147483647" to Int.MAX_VALUE, "-2147483648" to Int.MIN_VALUE,
            "" to 0, " " to 0, "12a" to 0, "1.2" to 0,
            "2147483648" to 0, "-2147483649" to 0).forEach { (input, expected) ->
            assertEquals(input, expected, input.toSafeInt())
            assertEquals(input, expected, intOf(input))
        }
    }

    @Test
    fun caseContainsIgnoresCase() {
        assertTrue("Hello".caseContains("HEL"))
        assertTrue("Hello".caseContains('H'))
        assertTrue("Hello".caseContains('h'))
        assertFalse("Hello".caseContains("world"))
    }

    @Test
    fun emailValidationRejectsWhitespaceAndMalformedDomains() {
        listOf("user@example.com", "first.last+tag@example.co.uk", "a_b@example.com").forEach {
            assertTrue(it, it.isValidEmail())
        }
        listOf("", "@example.com", "user@", "user@hello world", "user@example..com",
            "user@.com", "user@-example.com", "user@example.com ").forEach {
            assertFalse(it, it.isValidEmail())
        }
    }

    @Test
    fun censorReplacesEveryOccurrenceAndPreservesOtherText() {
        assertEquals("**** *** **** ****", "fuck sex porn fuck".censor())
        assertEquals("hello", "hello".censor())
        assertEquals("", "".censor())
    }

    @Test
    fun generatedStringsPreserveLengthAndUseExampleAlphabet() {
        listOf("abcdef", "ABCDEF", "012345", "abCD12!?", "!?@").forEach { example ->
            repeat(20) {
                val generated = example.generateMore()
                assertEquals(example.length, generated.length)
                val allowed = buildString {
                    if (example.any { it in LOWERS }) append(LOWERS)
                    if (example.any { it in UPPERS }) append(UPPERS)
                    if (example.any { it in DECIMALS }) append(DECIMALS)
                    if (example.any { it in SYMBOLS }) append(SYMBOLS)
                }
                assertTrue(generated.all { it in allowed })
            }
        }
        assertEquals("ooh!oh!", "".generateMore())
    }

    @Test
    fun generatedStringsWithUnsupportedCharactersDoNotCrash() {
        assertEquals("မြန်မာ", "မြန်မာ".generateMore())
        assertEquals("   ", "   ".generateMore())
    }

    @Test
    fun injectionPadsOnlyShorterValues() {
        assertEquals("0012", "0000".injectLast(12))
        assertEquals("1200", "0000".inject(12))
        assertEquals("12345", "0000".injectLast("12345"))
        assertEquals("12345", "0000".inject("12345"))
        assertEquals("abcd", "abcd".injectLast(""))
        assertEquals("abcd", "abcd".inject(""))
        assertEquals("hello", str(" hello "))
        assertEquals("hello", trim(" hello "))
    }

    @Test
    fun stringLoopsRespectOrderingAndEmptyInputs() {
        assertEquals("012", loopForString(3) { it.toString() })
        assertEquals("234", loopForString(2..4) { it.toString() })
        assertEquals("ab", loopForString(arrayListOf("a", "b")) { it })
        assertEquals("ab", loopForString(listOf("a", "b").iterator()) { it })
        assertEquals("ABC", loopForString("abc") { it.uppercase() })
        assertEquals("", loopForString(0) { "x" })
        assertEquals("", loopForString(IntRange.EMPTY) { "x" })
        assertEquals("", loopForString("") { "x" })
    }
}
