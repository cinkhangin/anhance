@file:Suppress("unused")

package com.naulian.anhance.models

data class AnResult(
    val isSuccessful : Boolean = true,
    val message : String = "Successful"
){
    companion object{
        val success = AnResult()
        val failure = AnResult(false , "failed")
    }
}
