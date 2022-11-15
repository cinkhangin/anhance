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

inline fun <T> check(value: T?, action : (nonNullValue : T) -> Unit){
    value?.let { action(it) }
}

//check if the value is null and continue if it is null
inline fun <T> ifNull(value: T?, action : () -> Unit){
    if(value == null) return
    action()
}

fun ifNotEmpty(string: String, action: (string: String) -> Unit) {
    if (string.isNotEmpty()) action(string)
}

fun ifEmpty(string: String, action: () -> Unit) {
    if (string.isEmpty()) action()
}

//return true is the value is null
fun <T> isNull(value : T) = value == null

//return true if the value is not null
fun <T> isNotNull(value: T) = value != null

//return the opposite value of a boolean
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

inline fun loopForString(times : Int, block: (index: Int) -> String): String {
    var output = ""
    repeat(times) {
        output += block(it)
    }
    return output
}


inline fun loopForString(range: IntRange, block: (index: Int) -> String): String {
    var output = ""
    for (i in range) {
        output += block(i)
    }
    return output
}

inline fun <T> loopForString(list : ArrayList<T>, block: (element: T) -> String): String {
    var output = ""
    for (element in list) {
        output += block(element)
    }
    return output
}

inline fun <T> loopForString(iterator : Iterator<T>, block: (element: T) -> String): String {
    var output = ""
    while (iterator.hasNext()){
        output += block(iterator.next())
    }
    return output
}

inline fun loopForString(string : String, block: (char: Char) -> String): String {
    var output = ""
    for (char in string) {
        output += block(char)
    }
    return output
}

inline fun <T> forEach(list: Iterable<T>, action: (T) -> Unit) {
    for (element in list) action(element)
}