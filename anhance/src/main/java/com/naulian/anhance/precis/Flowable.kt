@file:Suppress("unused")

package com.naulian.anhance.precis

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

fun <T> CoroutineScope.onEachLaunchIn(flowable: Flowable<T>, action: suspend (T) -> Unit) {
    flowable.data.onEach { action(it) }.launchIn(this)
}

fun <T> CoroutineScope.observe(flowable: Flowable<T>, action: suspend (T) -> Unit) {
    flowable.data.onEach { action(it) }.launchIn(this)
}

class Flowable<T>(val defValue : T){

    private val mutableData = MutableStateFlow(defValue)
    val data = mutableData.asStateFlow()

    fun update(newValue : T){
        mutableData.value = newValue
    }

    fun reset(){
        mutableData.value = defValue
    }

    operator fun invoke() = data
    operator fun invoke(scope: CoroutineScope, action: suspend (T) -> Unit){
        data.onEach { action(it) }.launchIn(scope)
    }
}