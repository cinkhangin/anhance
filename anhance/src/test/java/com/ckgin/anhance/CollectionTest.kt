package com.ckgin.anhance

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.TreeMap

class CollectionTest {
    @Test
    fun convertedCollectionsAreIndependentCopies() {
        val source = mutableListOf("a", "b")
        val copy = source.toArrayList()
        source.add("c")
        assertEquals(listOf("a", "b"), copy)
        copy.clear()
        assertEquals(3, source.size)
        assertEquals(emptyList<String>(), emptyList<String>().toArrayList())
        assertEquals(setOf(1, 2), hashMapOf("a" to 1, "b" to 2).toArrayList().toSet())
        assertEquals(listOf(1, 2), TreeMap(mapOf("b" to 2, "a" to 1)).toArrayList())
    }

    @Test
    fun selectByReplacesSelectionWithoutMutatingOriginal() {
        val original = listOf("a", "b", "c").toSelectableItemList()
        val selected = original.selectBy { it == "b" }
        assertEquals(listOf(false, true, false), selected.map { it.selected })
        assertEquals(listOf(false, false, false), original.map { it.selected })
        assertEquals(original, selected.selectBy { false })
    }

    @Test
    fun selectAllAndMultiSelectPreserveItemsAndOrder() {
        val original = listOf(1, 2, 3).toSelectableItemList()
        val all = original.selectAll(true)
        assertTrue(all.all { it.selected })
        assertEquals(listOf(1, 2, 3), all.map { it.item })
        assertEquals(listOf(true, false, true), all.multiSelectBy { it == 2 }.map { it.selected })
        assertEquals(all, all.multiSelectBy { it == 2 }.multiSelectBy { it == 2 })
        assertEquals(original, all.selectAll(false))
        assertEquals(emptyList<SelectableItem<Int>>(), emptyList<Int>().toSelectableItemList().selectAll(true))
    }

    @Test
    fun nullableHelpersInvokeOnlyApplicableCallbacks() {
        var calls = 0
        ifNotNull(null as String?) { calls++ }
        ifNull("value") { calls++ }
        assertEquals(0, calls)
        ifNotNull("value") {
            assertEquals("value", it)
            calls++
        }
        ifNull(null) { calls++ }
        assertEquals(2, calls)
        assertEquals("value", nullable("value"))
        assertEquals("value", nonNull("value"))
        org.junit.Assert.assertThrows(NullPointerException::class.java) { nonNull(null as String?) }
    }

    @Test
    fun resultHelpersPreserveValuesAndCauses() {
        val error = IllegalStateException("failure")
        assertEquals("ok", success("ok").getOrThrow())
        assertTrue(failure<String>(error).exceptionOrNull() === error)
        assertEquals("bad", failMessage<String>("bad").exceptionOrNull()!!.message)
        assertFalse(success(null).isFailure)
    }

    @Test
    fun genericLoopsAndEmptyCallbacksRespectInputs() {
        assertEquals(6, loopForValue(1..3, 0) { value, total -> total + value })
        assertEquals(7, loopForValue(IntRange.EMPTY, 7) { _, _ -> 0 })
        val visited = mutableListOf<Int>()
        forEach(listOf(1, 2, 3)) { visited.add(it) }
        assertEquals(listOf(1, 2, 3), visited)
        var calls = 0
        ifEmpty("value") { calls++ }
        ifNotEmpty("") { calls++ }
        ifEmpty("") { calls++ }
        ifNotEmpty("value") {
            assertEquals("value", it)
            calls++
        }
        assertEquals(2, calls)
        assertEquals("yyyy MM dd", Pattern.generatePattern(Pattern.YEAR_FULL, Pattern.SPACE,
            Pattern.MONTH2, Pattern.SPACE, Pattern.DAY2))
        assertEquals("", Pattern.generatePattern())
    }
}
