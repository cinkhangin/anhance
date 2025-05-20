package com.example.anhance

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.naulian.anhance.AudioPlayer

@Composable
fun AudioPlayerExample() {

    val context = LocalContext.current
    val keyPlayPause = "play_pause"
    val keyPlayStop = "play_stop"

    LaunchedEffect(Unit) {
        AudioPlayer.load(context, R.raw.be_the_change, keyPlayPause)
        AudioPlayer.load(context, R.raw.be_the_change, keyPlayStop)
    }

    Column(modifier = Modifier.padding(24.dp)) {

        Text(text = "Test Play and Pause")
        Row(modifier = Modifier, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(
                modifier = Modifier.weight(1f),
                onClick = {
                    AudioPlayer.play(key = keyPlayPause)
                }
            ) { Text(text = "Play") }

            Button(
                modifier = Modifier.weight(1f),
                onClick = {
                    AudioPlayer.pause(key = keyPlayPause)
                }
            ) { Text(text = "Pause") }
        }

        Text(text = "Test Play and Stop")
        Row(modifier = Modifier, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(
                modifier = Modifier.weight(1f),
                onClick = {
                    AudioPlayer.play(key = keyPlayStop)
                }
            ) { Text(text = "Play") }

            Button(
                modifier = Modifier.weight(1f),
                onClick = {
                    AudioPlayer.stop(key = keyPlayStop)
                }
            ) { Text(text = "Stop") }
        }
    }
}