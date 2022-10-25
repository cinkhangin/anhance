@file:Suppress("unused")

package com.naulian.anhance

fun <T> nonNull(any: T?): T {
    return any!!
}

//I'm not sure if I'd ever need this
fun <T> nullable(any: T): T? {
    return any
}

inline fun <T> ifNotNull(value: T?, action : (nonNullValue : T) -> Unit){
    value?.let { action(it) }
}

inline fun <T> ifNull(value: T?, action : () -> Unit){
    if(value == null) return
    action()
}

fun <T> isNull(value : T) = value == null
fun <T> isNotNull(value: T) = value != null

fun not(bool: Boolean): Boolean {
    return bool.not()
}

inline fun <T> loopForValue(
    range: IntRange,
    startValue: T,
    block: (index: Int, value: T) -> T
): T {
    var value: T = startValue
    for (i in range) {
        value = block(i, value)
    }

    return value
}

inline fun loopForString(range: IntRange, block: (index: Int) -> String): String {
    var string = ""
    for (i in range) {
        string += block(i)
    }
    return string
}