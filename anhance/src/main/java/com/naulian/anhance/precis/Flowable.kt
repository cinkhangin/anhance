package com.naulian.anhance.precis

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

fun <T> CoroutineScope.onEachLaunchIn(flowable: Flowable<T>, action: suspend (T) -> Unit) {
    flowable.data.onEach { action(it) }.launchIn(this)
}

class Flowable<T>(val defValue : T){

    private val mutableStateFlow = MutableStateFlow(defValue)
    val data = mutableStateFlow.asStateFlow()

    fun update(newValue : T){
        mutableStateFlow.value = newValue
    }

    fun reset(){
        mutableStateFlow.value = defValue
    }

    operator fun invoke(scope: CoroutineScope, action: suspend (T) -> Unit){
        data.onEach { action(it) }.launchIn(scope)
    }
}