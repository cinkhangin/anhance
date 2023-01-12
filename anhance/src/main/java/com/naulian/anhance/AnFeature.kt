@file:Suppress("unused")

package com.naulian.anhance


fun ifNotEmpty(string: String, action: (string: String) -> Unit) {
    if (string.isNotEmpty()) action(string)
}

fun ifEmpty(string: String, action: () -> Unit) {
    if (string.isEmpty()) action()
}

//return the opposite value of a boolean
fun not(bool: Boolean): Boolean {
    return bool.not()
}

inline fun <T> apply(data: T, block: T.() -> Unit) {
    data.apply { block(this) }
}

inline fun <T> use(data: T, block: (value : T) -> Unit) {
    data.apply { block(this) }
}

inline fun <T> loopForValue(
    range: IntRange, startValue: T,
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