@file:Suppress("unused")

package com.naulian.anhance.models

import com.naulian.anhance.isNotNull
import com.naulian.anhance.isNull

data class AnResult<T>(
    val data : T? = null,
    val message : String = "Successful",

){
    val isSuccessful get() = isNotNull(data)
    val isEmpty get() = isNull(data)

    companion object{
        val success = AnResult(true)
        val failure = AnResult(false , "failed")
    }
}
