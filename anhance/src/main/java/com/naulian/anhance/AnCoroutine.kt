@file:Suppress("unused")

package com.naulian.anhance

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

fun <T> Flow<T>.onEachLaunchIn(scope: CoroutineScope, action: suspend (T) -> Unit) {
    this.onEach { action(it) }.launchIn(scope)
}

fun <T> CoroutineScope.onEachLaunch(data: Flow<T>, action: suspend (T) -> Unit) {
    data.onEachLaunchIn(this) { action(it) }
}

fun Application.applicationScope(action: suspend CoroutineScope.() -> Unit) {
    CoroutineScope(SupervisorJob()).launch {
        action(this)
    }
}

fun AppCompatActivity.activityScope(action: suspend CoroutineScope.() -> Unit) {
    lifecycleScope.launch {
        action(this)
    }
}

