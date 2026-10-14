package com.ckgin.anhance

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CoroutineTest {
    @Test
    fun applicationScopeCompletesItsActionAndChildren() = runTest {
        val value = CompletableDeferred<Int>()
        val job = applicationScope {
            launch { value.complete(42) }
        }
        job.join()
        assertEquals(42, value.await())
        assertFalse(job.isActive)
    }

    @Test
    fun observationHelpersCollectValuesInOrder() = runTest {
        val first = mutableListOf<Int>()
        val second = mutableListOf<Int>()
        onEachLaunch(flowOf(1, 2, 3)) { first.add(it) }.join()
        observe(flowOf(1, 2, 3)) { second.add(it) }.join()
        assertEquals(listOf(1, 2, 3), first)
        assertEquals(first, second)
    }

    @Test
    fun cancellingObservationStopsCollection() = runTest {
        val flow = MutableSharedFlow<Int>()
        val values = mutableListOf<Int>()
        val job = observe(flow) { values.add(it) }
        runCurrent()
        flow.emit(1)
        runCurrent()
        job.cancel()
        job.join()
        flow.emit(2)
        assertEquals(listOf(1), values)
        assertFalse(job.isActive)
    }

    @Test
    fun toastClearsAfterRequestedDuration() = runTest {
        val job = launch { FlowToast.showErr("failed", 1000) }
        runCurrent()
        assertEquals(ToastMessage("failed", true), FlowToast.message.value)
        advanceTimeBy(999)
        assertEquals("failed", FlowToast.message.value.text)
        advanceTimeBy(1)
        runCurrent()
        assertEquals(ToastMessage.Empty, FlowToast.message.value)
        job.join()
    }

    @Test
    fun olderToastCannotClearNewerToastEvenWithIdenticalText() = runTest {
        launch { FlowToast.show("same", 1000) }
        runCurrent()
        advanceTimeBy(500)
        launch { FlowToast.show("same", 1000) }
        runCurrent()
        advanceTimeBy(500)
        runCurrent()
        assertEquals(ToastMessage("same"), FlowToast.message.value)
        advanceTimeBy(500)
        runCurrent()
        assertEquals(ToastMessage.Empty, FlowToast.message.value)
    }

    @Test
    fun cancellingToastClearsIt() = runTest {
        val job = launch { FlowToast.show("cancel", 1000) }
        runCurrent()
        job.cancel()
        job.join()
        assertEquals(ToastMessage.Empty, FlowToast.message.value)
    }

    @Test
    fun cancellingOlderToastPreservesNewerMessage() = runTest {
        val old = launch { FlowToast.show("old", 1000) }
        runCurrent()
        val current = launch { FlowToast.show("new", 1000) }
        runCurrent()
        old.cancel()
        old.join()
        assertEquals("new", FlowToast.message.value.text)
        current.cancel()
        current.join()
    }
}
