@file:Suppress("unused")

package com.naulian.anhance

import android.content.Context
import androidx.fragment.app.Fragment

//Temporary class to destroy later
fun <T> loadUi(binding: AnBinding<T>, action: T.() -> Unit) = action(binding.value)
fun <T> Fragment.loadUi(
    binding: AnBinding<T>,
    action: T.(context: Context) -> Unit
) = action(binding.value, requireContext())


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