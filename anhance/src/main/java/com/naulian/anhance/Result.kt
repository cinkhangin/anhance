@file:Suppress("unused")

package com.naulian.anhance

fun <T> success(value: T): Result<T> = Result.success(value)
fun <T> failure(exception: Exception): Result<T> = Result.failure(exception)
fun <T> failMessage(message: String): Result<T> =
    Result.failure(Exception(message))