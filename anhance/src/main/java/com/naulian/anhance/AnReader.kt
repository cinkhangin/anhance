@file:Suppress("unused")

package com.naulian.anhance

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.*

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

    fun read(text: String, speechRate: Float = 1.0f) {
        textToSpeech?.setSpeechRate(speechRate)
        textToSpeech?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "tts")
    }

    fun initialize(context: Context, speechRate: Float = 1.0f) {
        textToSpeech?.let {
            return
        }

        val speechListener = TextToSpeech.OnInitListener {
            if (it == TextToSpeech.SUCCESS) {
                textToSpeech?.language = Locale.US
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
                boostPerformance()
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