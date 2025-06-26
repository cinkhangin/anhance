@file:Suppress("unused")

package com.naulian.anhance

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log

sealed interface SpeechToTextResult {
    data object Ready : SpeechToTextResult
    data object Begin : SpeechToTextResult
    data class Partial(val text: String) : SpeechToTextResult
    data class Result(val text: String) : SpeechToTextResult
    data class Error(val message: String) : SpeechToTextResult
    data object End : SpeechToTextResult
}

private typealias RI = RecognizerIntent
object SpeechListener {
    private val TAG = SpeechListener::class.java.simpleName

    private var listening = false
    private lateinit var speechRecognizer: SpeechRecognizer

    private val initialized get() = ::speechRecognizer.isInitialized

    fun observe(context: Context, wordCount: Int = 1, action: (SpeechToTextResult) -> Unit) {
        val isAvailable = SpeechRecognizer.isRecognitionAvailable(context)
        if (isAvailable) {
            Log.i(TAG, "initialize: SpeechRecognizer is available")
            setup(context, wordCount, action)
            return
        }

        Log.e(TAG, "initialize: SpeechRecognizer is not available")
    }

    private fun setup(context: Context, wordCount : Int = 1, action: (SpeechToTextResult) -> Unit) {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
        val recognitionListener = object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {
                action(SpeechToTextResult.Ready)
            }

            override fun onBeginningOfSpeech() {
                action(SpeechToTextResult.Begin)
            }

            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}

            override fun onPartialResults(partialResults: Bundle) {
                val voiceResults = partialResults.getStringArrayList(
                    SpeechRecognizer.RESULTS_RECOGNITION
                )
                action(SpeechToTextResult.Partial(voiceResults?.joinToString(" ") ?: ""))
            }

            override fun onResults(results: Bundle) {
                val voiceResults = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                action(SpeechToTextResult.Partial(voiceResults?.joinToString(" ") ?: ""))
            }

            override fun onError(error: Int) {
                val message = when (error) {
                    SpeechRecognizer.ERROR_AUDIO -> "Audio recording error"
                    SpeechRecognizer.ERROR_CLIENT -> "Client side error"
                    SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Insufficient permissions"
                    SpeechRecognizer.ERROR_NETWORK -> "Network error"
                    SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Network timeout"
                    SpeechRecognizer.ERROR_NO_MATCH -> "No match found"
                    SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "Recognition service busy"
                    SpeechRecognizer.ERROR_SERVER -> "Server error"
                    SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "Speech timeout"
                    else -> "Unknown error (code:$error)"
                }
                action(SpeechToTextResult.Error(message))
                Log.e(TAG, "onError: $message")
            }

            override fun onEndOfSpeech() {
                action(SpeechToTextResult.End)
                stopListening()
            }

            override fun onEvent(eventType: Int, params: Bundle?) {}
        }

        speechRecognizer.setRecognitionListener(recognitionListener)
    }

    fun startListening(context : Context, wordCount: Int = 1) {
        if (listening) {
           stopListening()
        }

        val speechIntent = Intent(RI.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RI.EXTRA_LANGUAGE_MODEL, RI.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RI.EXTRA_LANGUAGE, "en-US")
            putExtra(RI.EXTRA_CALLING_PACKAGE, context.packageName)
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, wordCount)
        }

        speechRecognizer.startListening(speechIntent)
        listening = true
    }

    fun stopListening() {
        if (!listening) {
            return
        }

        speechRecognizer.stopListening()
        listening = false
    }

    fun destroy() {
        listening = false
        speechRecognizer.stopListening()
        speechRecognizer.destroy()
    }
}