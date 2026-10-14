@file:Suppress("unused")

package com.ckgin.anhance

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

fun initializeReader(context: Context, speechRate: Float = 1.0f) {
    TextReader.initialize(context, speechRate)
}

fun readText(text: String, onError: (String) -> Unit = {}) {
    TextReader.read(text, onError)
}

fun readText(text: String, speechRate: Float, onError: (String) -> Unit = {}) {
    TextReader.read(text, speechRate, onError)
}

object TextReader {
    private var textToSpeech: TextToSpeech? = null

    const val STATE_INITIALIZE = 0
    const val STATE_READING = 1
    const val STATE_DONE = 2
    const val STATE_IDLE = 3

    private var isInitialized = MutableStateFlow(false)
    val isInitializedFlow = isInitialized.asStateFlow()

    private var utteranceProgress = MutableStateFlow(STATE_INITIALIZE)
    val utteranceProgressFlow = utteranceProgress.asStateFlow()

    private var speechRate = 1.0f
    private var error: String? = null
    private var generation = 0L

    fun read(text: String, onError: (String) -> Unit = {}) {
        read(text, speechRate, onError)
    }

    fun read(text: String, speechRate: Float, onError: (String) -> Unit = {}) {
        val reader = textToSpeech
        val failure = error
        if (failure != null) {
            onError(failure)
        } else if (!isInitialized.value || reader == null) {
            onError("Text-to-Speech is not initialized")
        } else if (reader.setSpeechRate(speechRate) == TextToSpeech.ERROR ||
            reader.speak(text, TextToSpeech.QUEUE_FLUSH, null, "tts") == TextToSpeech.ERROR
        ) {
            onError("Cannot read text")
        }
    }

    fun initialize(context: Context, speechRate: Float = 1.0f) {
        if (textToSpeech != null && error == null) return
        if (textToSpeech != null) destroy()
        val token = ++generation
        error = null
        utteranceProgress.value = STATE_INITIALIZE

        val speechListener = TextToSpeech.OnInitListener {
            if (token != generation) return@OnInitListener
            if (it == TextToSpeech.SUCCESS) {
                textToSpeech?.language = Locale.US

                val result = textToSpeech?.setLanguage(Locale.ENGLISH)

                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    context.showToast("Language is not supported.")
                    error = "Language is not supported."
                    return@OnInitListener
                }

                this.speechRate = speechRate
                textToSpeech?.setSpeechRate(speechRate)

                val utteranceListener = object : UtteranceProgressListener() {
                    override fun onStart(utteranceId: String?) {
                        if (token == generation) utteranceProgress.value = STATE_READING
                    }

                    override fun onDone(utteranceId: String?) {
                        if (token == generation) utteranceProgress.value = STATE_DONE
                    }

                    @Deprecated("Deprecated in Java")
                    override fun onError(utteranceId: String?) {
                        if (token == generation) utteranceProgress.value = STATE_IDLE
                    }
                }

                textToSpeech?.setOnUtteranceProgressListener(utteranceListener)
                isInitialized.value = true
                error = null
                boostPerformance()
            } else {
                context.showToast("Text-to-Speech is not available")
                error = "Text-to-Speech is not available"
            }
        }
        textToSpeech = TextToSpeech(context.applicationContext, speechListener)
    }

    fun check(context: Context) {
        if (isInitialized.value) return

        initialize(context)
    }

    fun destroy() {
        generation++
        textToSpeech?.shutdown()
        textToSpeech = null
        isInitialized.value = false
        utteranceProgress.value = STATE_INITIALIZE
        error = null
    }

    fun stop() {
        textToSpeech?.stop()
        utteranceProgress.value = STATE_IDLE
    }

    fun reset() {
        utteranceProgress.value = STATE_IDLE
    }

    //to fix texttospeech delay on first call
    private fun boostPerformance() {
        read("")
    }
}
