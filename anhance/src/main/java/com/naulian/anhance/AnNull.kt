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
inline fun <T> ifNotNull(value: T?, action : (nonNullValue : T) -> Unit){
    value?.let { action(it) }
}

inline fun <T> nullCheck(value: T?, action : (nonNullValue : T) -> Unit){
    value?.let { action(it) }
}

inline fun <T> failure(value: T?, action : (nonNullValue : T) -> Unit){
    value?.let { action(it) }
}

inline fun <T> success(value: T?, action : (nonNullValue : T) -> Unit){
    value?.let { action(it) }
}

//check if the value is null and continue if it is null
inline fun <T> ifNull(value: T?, action : () -> Unit){
    if(value == null) return
    action()
}

//return true is the value is null
fun <T> isNull(value : T) = value == null

//return true if the value is not null
fun <T> isNotNull(value: T) = value != null