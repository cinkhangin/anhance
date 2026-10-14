package com.ckgin.anhance

import android.content.Context
import android.content.pm.ResolveInfo
import android.content.pm.ServiceInfo
import android.content.Intent
import android.os.Bundle
import android.os.Looper
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowSpeechRecognizer

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28, 35])
class SpeechListenerTest {
    private lateinit var context: Context
    private val results = mutableListOf<SpeechToTextResult>()

    @Before
    fun setUp() {
        context = RuntimeEnvironment.getApplication()
        SpeechListener.destroy()
        val service = ResolveInfo().apply {
            serviceInfo = ServiceInfo().apply {
                packageName = "test.recognition"
                name = "RecognitionService"
            }
        }
        shadowOf(context.packageManager).addResolveInfoForIntent(
            Intent("android.speech.RecognitionService"), service
        )
    }

    @After
    fun tearDown() {
        SpeechListener.destroy()
    }

    private fun start(): org.robolectric.shadows.ShadowSpeechRecognizer {
        SpeechListener.observe(context) { results.add(it) }
        SpeechListener.startListening(context)
        shadowOf(Looper.getMainLooper()).idle()
        return shadowOf(ShadowSpeechRecognizer.getLatestSpeechRecognizer())
    }

    @Test
    fun partialAndFinalResultsUseBestHypothesisAndDistinctStates() {
        val recognizer = start()
        val bundle = Bundle().apply {
            putStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION, arrayListOf("hello", "yellow"))
        }
        recognizer.triggerOnPartialResults(bundle)
        recognizer.triggerOnResults(bundle)
        assertEquals(listOf(SpeechToTextResult.Partial("hello"), SpeechToTextResult.Result("hello")), results)
    }

    @Test
    fun missingResultsProduceEmptyFinalText() {
        start().triggerOnResults(Bundle())
        assertEquals(SpeechToTextResult.Result(""), results.last())
    }

    @Test
    fun readyEndAndErrorsAreDelivered() {
        val recognizer = start()
        recognizer.triggerOnReadyForSpeech(Bundle())
        recognizer.triggerOnEndOfSpeech()
        recognizer.triggerOnError(SpeechRecognizer.ERROR_NO_MATCH)
        assertEquals(listOf(SpeechToTextResult.Ready, SpeechToTextResult.End,
            SpeechToTextResult.Error("No match found")), results)
        SpeechListener.startListening(context)
        shadowOf(Looper.getMainLooper()).idle()
        assertEquals(RecognizerIntent.LANGUAGE_MODEL_FREE_FORM,
            recognizer.lastRecognizerIntent.getStringExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL))
    }

    @Test
    fun observingAgainReleasesPreviousRecognizer() {
        val old = start()
        SpeechListener.observe(context) { results.add(it) }
        assertTrue(old.isDestroyed)
    }

    @Test
    fun destroyAndStopAreSafeBeforeSetupAndRepeatedly() {
        SpeechListener.destroy()
        SpeechListener.stopListening()
        SpeechListener.destroy()
        SpeechListener.startListening(context)
    }
}
