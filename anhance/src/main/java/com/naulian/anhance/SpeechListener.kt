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
    data object Failure : SpeechToTextResult
    data class Partial(val text: String) : SpeechToTextResult
    data class Result(val text: String) : SpeechToTextResult
    data class Error(val message: String) : SpeechToTextResult
    data object End : SpeechToTextResult
}

object SpeechListener {
    private val TAG = SpeechListener::class.java.simpleName

    private var listening = false
    private lateinit var speechRecognizer: SpeechRecognizer

    private val initialized get() = ::speechRecognizer.isInitialized

    fun initialize(context: Context) {
        val isAvailable = SpeechRecognizer.isRecognitionAvailable(context)
        if (isAvailable) {
            Log.i(TAG, "initialize: SpeechRecognizer is available")
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
            return
        }
    }

    fun listen(context: Context, action: (SpeechToTextResult) -> Unit) {
        if (!initialized) {
            action(SpeechToTextResult.Failure)
            return
        }

        if (listening) destroy()
        val recognitionListener = object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {
                action(SpeechToTextResult.Ready)
            }

            override fun onBeginningOfSpeech() {
                action(SpeechToTextResult.Begin)
            }

            override fun onRmsChanged(rmsdB: Float) {

            }

            override fun onBufferReceived(buffer: ByteArray?) {

            }

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
                    SpeechRecognizer.ERROR_NO_MATCH -> "Try Again!"
                    SpeechRecognizer.ERROR_NETWORK -> "Connection Error"
                    else -> "Error Code $error"
                }
                action(SpeechToTextResult.Error(message))
                logError(TAG, error)
            }

            override fun onEndOfSpeech() {
                action(SpeechToTextResult.End)
            }

            override fun onEvent(eventType: Int, params: Bundle?) {

            }
        }

        val speechIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        speechIntent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        speechIntent.putExtra(
            RecognizerIntent.EXTRA_CALLING_PACKAGE, context.packageName
        )

        speechRecognizer.setRecognitionListener(recognitionListener)
        speechRecognizer.startListening(speechIntent)
        listening = true
    }

    fun destroy() {
        listening = false
        speechRecognizer.stopListening()
        speechRecognizer.destroy()
    }
}