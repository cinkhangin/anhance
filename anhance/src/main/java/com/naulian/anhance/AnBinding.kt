@file:Suppress("unused")

package com.naulian.anhance

//Temporary class to destroy it later
fun <T> loadUi(binding: AnBinding<T>, action: T.() -> Unit) = action(binding.value)


class AnBinding<T> {

    private var binding: T? = null
    val value get() = nonNull(binding)

    fun assign(binding: T) {
        this.binding = binding
    }

    fun destroy() {
        binding = null
    }

    operator fun invoke(action: T.() -> Unit) = action(value)

}