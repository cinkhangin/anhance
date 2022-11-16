@file:Suppress("unused")

package com.naulian.anhance

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
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
    data.onEach { action(it) }.launchIn(this)
}

fun <T> CoroutineScope.observe(data: Flow<T>, action: suspend (T) -> Unit) {
    data.onEach { action(it) }.launchIn(this)
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

fun Fragment.fragmentOnStartScope(block: suspend CoroutineScope.() -> Unit) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            block(this@repeatOnLifecycle)
        }
    }
}

