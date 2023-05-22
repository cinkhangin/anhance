@file:Suppress("unused")

package com.naulian.anhance

fun <T> check(any: T?, action: (T) -> Unit) {
    any?.let { action(it) }
}

fun <A, B> check(a: A?, b: B?, action: (A, B) -> Unit) {
    a?.let { aa -> b?.let { bb -> action(aa, bb) } }
}

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

inline fun <T> nullCheck(value: T?, action : (T) -> Unit){
    value?.let { action(it) }
}

//check if the value is null and continue if it is null
inline fun <T> ifNull(value: T?, action : () -> Unit){
    if(value == null) action()
}

//return true is the value is null
fun <T> isNull(value : T) = value == null

//return true if the value is not null
fun <T> isNotNull(value: T) = value != null