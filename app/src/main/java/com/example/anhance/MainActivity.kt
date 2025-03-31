package com.example.anhance

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.naulian.anhance.AudioPlayer
import com.naulian.anhance.initializeReader


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeReader(this)

        setContent {
            MaterialTheme {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

                    val context = LocalContext.current
                    var buttonText by remember { mutableStateOf("Listen") }

                    LaunchedEffect(Unit) {
                        AudioPlayer.load(context, R.raw.be_the_change)
                    }

                    Button(onClick = {
                        AudioPlayer.play(context = context, resId = R.raw.be_the_change){
                            buttonText = "Listen"
                        }
                        buttonText = "Playing"
                    }) {
                        Text(text = buttonText)
                    }
                }
            }
        }
    }
}