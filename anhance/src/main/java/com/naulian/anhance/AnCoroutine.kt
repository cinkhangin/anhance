@file:Suppress("unused")

package com.naulian.anhance

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

fun <T> Flow<T>.onEachLaunchIn(scope: CoroutineScope, action: suspend (T) -> Unit){
    this.onEach { action(it) }.launchIn(scope)
}

