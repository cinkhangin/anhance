package com.ckgin.anhance

import android.content.Context
import android.speech.tts.TextToSpeech
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowTextToSpeech
import java.util.Locale

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28, 35])
class TextReaderTest {
    private lateinit var context: Context

    @Before
    fun setUp() {
        context = RuntimeEnvironment.getApplication()
        TextReader.destroy()
        ShadowTextToSpeech.addLanguageAvailability(Locale.ENGLISH)
    }

    @After
    fun tearDown() {
        TextReader.destroy()
    }

    private fun initialize(): org.robolectric.shadows.ShadowTextToSpeech {
        initializeReader(context)
        val shadow = shadowOf(ShadowTextToSpeech.getLastTextToSpeechInstance())
        shadow.onInitListener.onInit(TextToSpeech.SUCCESS)
        return shadow
    }

    @Test
    fun readsBeforeInitializationReportAnError() {
        val errors = mutableListOf<String>()
        readText("hello") { errors.add(it) }
        assertEquals(1, errors.size)
        assertTrue(errors.single().isNotEmpty())
    }

    @Test
    fun successfulInitializationReadsAndTracksProgress() {
        val shadow = initialize()
        assertTrue(TextReader.isInitializedFlow.value)
        readText("hello")
        assertEquals("hello", shadow.lastSpokenText)
        assertEquals(TextToSpeech.QUEUE_FLUSH, shadow.queueMode)
        shadow.utteranceProgressListener.onStart("tts")
        assertEquals(TextReader.STATE_READING, TextReader.utteranceProgressFlow.value)
        shadow.utteranceProgressListener.onDone("tts")
        assertEquals(TextReader.STATE_DONE, TextReader.utteranceProgressFlow.value)
        TextReader.stop()
        assertTrue(shadow.isStopped)
        assertEquals(TextReader.STATE_IDLE, TextReader.utteranceProgressFlow.value)
    }

    @Test
    fun destroyClearsStateAndAllowsReinitialization() {
        val first = initialize()
        first.utteranceProgressListener.onStart("tts")
        TextReader.destroy()
        assertTrue(first.isShutdown)
        assertFalse(TextReader.isInitializedFlow.value)
        assertEquals(TextReader.STATE_INITIALIZE, TextReader.utteranceProgressFlow.value)
        initialize()
        assertTrue(TextReader.isInitializedFlow.value)
    }

    @Test
    fun initializationFailureIsReportedAndClearedOnRetry() {
        initializeReader(context)
        val failed = shadowOf(ShadowTextToSpeech.getLastTextToSpeechInstance())
        failed.onInitListener.onInit(TextToSpeech.ERROR)
        val errors = mutableListOf<String>()
        readText("hello") { errors.add(it) }
        assertEquals(1, errors.size)
        val ready = initialize()
        assertTrue(failed.isShutdown)
        readText("retry") { errors.add(it) }
        assertEquals(1, errors.size)
        assertEquals("retry", ready.lastSpokenText)
    }

    @Test
    fun callbacksFromDestroyedReadersCannotChangeCurrentState() {
        initializeReader(context)
        val pending = shadowOf(ShadowTextToSpeech.getLastTextToSpeechInstance())
        TextReader.destroy()
        pending.onInitListener.onInit(TextToSpeech.SUCCESS)
        assertFalse(TextReader.isInitializedFlow.value)
        val old = initialize()
        TextReader.destroy()
        old.utteranceProgressListener.onStart("tts")
        assertEquals(TextReader.STATE_INITIALIZE, TextReader.utteranceProgressFlow.value)
    }
}
