@file:Suppress("unused")

package com.naulian.anhance

//convert nullable value to non-nullable value
fun <T> nonNull(any: T?): T {
    return any!!
}

//convert non-nullable value to nullable value
fun <T> nullable(any: T): T? {
    return any
}

//check if the value is not null and continue if it is not null
inline fun <T> ifNotNull(value: T?, action : (T) -> Unit){
    value?.let { action(it) }
}

//check if the value is null and continue if it is null
inline fun <T> ifNull(value: T?, action : () -> Unit){
    if(value == null) action()
}