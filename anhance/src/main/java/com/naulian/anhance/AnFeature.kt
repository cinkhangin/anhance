@file:Suppress("unused")

package com.naulian.anhance


fun ifNotEmpty(string: String, action: (String) -> Unit) {
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

inline fun <T> use(data: T, block: (T) -> Unit) {
    data.apply { block(this) }
}

inline fun <T> loopForValue(
    range: IntRange, startValue: T,
    block: (Int, T) -> T
): T {
    var value: T = startValue
    for (i in range) {
        value = block(i, value)
    }

    return value
}

inline fun <T> forEach(list: Iterable<T>, action: (T) -> Unit) {
    for (element in list) action(element)
}