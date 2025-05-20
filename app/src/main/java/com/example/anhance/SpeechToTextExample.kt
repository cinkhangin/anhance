package com.example.anhance

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.naulian.anhance.SpeechListener
import com.naulian.anhance.SpeechToTextResult

@Composable
fun SpeechToTextExample() {
    val context = LocalContext.current

    // Track permission status
    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    // Permission launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasPermission = isGranted
    }

    LaunchedEffect(hasPermission) {
        if (!hasPermission) {
            permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }
    }

    var text by remember { mutableStateOf("") }

    Text(text = text)
    Spacer(modifier = Modifier.height(16.dp))
    Button(
        onClick = {
            SpeechListener.listen(context = context) { state ->
                text = when (state) {
                    SpeechToTextResult.Begin -> "Listening"
                    SpeechToTextResult.End -> "Closed"
                    is SpeechToTextResult.Error -> state.message
                    SpeechToTextResult.Failure -> "Failure"
                    is SpeechToTextResult.Partial -> state.text
                    SpeechToTextResult.Ready -> "Ready"
                    is SpeechToTextResult.Result -> state.text
                }
            }
        }
    ) {
        Text(text = "Listen")
    }
}

