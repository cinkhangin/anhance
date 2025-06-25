package com.example.anhance

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.naulian.anhance.SpeechListener
import com.naulian.anhance.initializeReader
import java.io.File
import java.io.FileOutputStream
import kotlin.random.Random
import kotlin.random.nextInt


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeReader(this)

        SpeechListener.initialize(this)

        val cacheFile = File(cacheDir, "audio.mp3")

        if (!cacheFile.exists()) {
            assets.open("audio.mp3").use { inputStream ->
                FileOutputStream(cacheFile).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
        }

        setContent {
            MaterialTheme {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    AudioPlayerExample()
                }
            }
        }
    }
}