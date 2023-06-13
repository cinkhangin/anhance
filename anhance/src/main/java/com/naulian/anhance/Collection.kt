@file:Suppress("unused")

package com.naulian.anhance

import java.util.*

fun <T> List<T>.toArrayList() = ArrayList(this)
fun <K, V> HashMap<K, V>.toArrayList() = ArrayList(this.values)
fun <K, V> SortedMap<K, V>.toArrayList() = ArrayList(this.values)
