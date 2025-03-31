@file:Suppress("unused")

package com.naulian.anhance

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer

enum class EarState {
    READY, BEGIN, VOLUME, PARTIAL, RESULT, ERROR, END
}

object AnEar {
    private val TAG = AnEar::class.java.simpleName

    private var listening = false
    private var speechRecognizer: SpeechRecognizer? = null

    fun listen(context: Context, action: (EarState, String) -> Unit) {
        if (listening) destroy()

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
        val recognitionListener = object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {
                action(EarState.READY, "Ready")
            }

            override fun onBeginningOfSpeech() {
                action(EarState.BEGIN, "Listening")
            }

            override fun onRmsChanged(rmsdB: Float) {

            }

            override fun onBufferReceived(buffer: ByteArray?) {

            }

            override fun onPartialResults(partialResults: Bundle) {
                val voiceResults = partialResults.getStringArrayList(
                    SpeechRecognizer.RESULTS_RECOGNITION
                )
                action(EarState.PARTIAL, voiceResults?.joinToString("\n") ?: "")
            }

            override fun onResults(results: Bundle) {
                val voiceResults = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                action(EarState.RESULT, voiceResults?.joinToString("\n") ?: "")
            }

            override fun onError(error: Int) {
                val message = when (error) {
                    SpeechRecognizer.ERROR_NO_MATCH -> "Try Again!"
                    SpeechRecognizer.ERROR_NETWORK -> "Connection Error"
                    else -> "Error Code $error"
                }
                action(EarState.ERROR, message)
                logError(TAG, error)
            }

            override fun onEndOfSpeech() {
                action(EarState.END, "Good Job")
            }

            override fun onEvent(eventType: Int, params: Bundle?) {

            }
        }

        val speechIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, context.packageName)
        }

        speechRecognizer?.setRecognitionListener(recognitionListener)
        speechRecognizer?.startListening(speechIntent)
        listening = true
    }

    fun destroy() {
        listening = false
        speechRecognizer?.stopListening()
        speechRecognizer?.destroy()
    }
}