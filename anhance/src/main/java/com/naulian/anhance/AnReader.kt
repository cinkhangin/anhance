@file:Suppress("unused")

package com.naulian.anhance

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

fun initializeReader(context: Context, speechRate: Float = 1.0f) {
    AnReader.initialize(context, speechRate)
}

fun readText(text: String, onError: (String) -> Unit = {}) {
    AnReader.read(text, onError)
}

fun readText(text: String, speechRate: Float, onError: (String) -> Unit = {}) {
    AnReader.read(text, speechRate, onError)
}

object AnReader {
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

    fun read(text: String, onError: (String) -> Unit = {}) {
        read(text, speechRate, onError)
    }

    fun read(text: String, speechRate: Float, onError: (String) -> Unit = {}) {
        error?.let { onError(it) } ?: kotlin.run {
            textToSpeech?.setSpeechRate(speechRate)
            textToSpeech?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "tts")
        }
    }

    fun initialize(context: Context, speechRate: Float = 1.0f) {
        textToSpeech?.let {
            return
        }

        val speechListener = TextToSpeech.OnInitListener {
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
                        utteranceProgress.value = STATE_READING
                    }

                    override fun onDone(utteranceId: String?) {
                        utteranceProgress.value = STATE_DONE
                    }

                    @Deprecated("Deprecated in Java")
                    override fun onError(utteranceId: String?) {
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
        textToSpeech = TextToSpeech(context, speechListener)
    }

    fun check(context: Context) {
        if (isInitialized.value) return

        initialize(context)
    }

    fun destroy() {
        textToSpeech?.shutdown()
        textToSpeech = null
        isInitialized.value = false
    }

    fun stop() {
        textToSpeech?.stop()
    }

    fun reset() {
        utteranceProgress.value = STATE_IDLE
    }

    //to fix texttospeech delay on first call
    private fun boostPerformance() {
        read("")
    }
}