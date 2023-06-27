@file:Suppress("unused")

package com.naulian.anhance

import android.content.Context
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

object Json {
    val moshi: Moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    inline fun <reified T> parseItems(context: Context, filename: String): List<T> {
        val json = context.readStringAsset(filename)
        return fromJson(json)
    }

    inline fun <reified T> fromJson(txt: String): List<T> {
        val type = Types.newParameterizedType(List::class.java, T::class.java)
        val adapter = moshi.adapter<List<T>>(type)
        return adapter.fromJson(txt) ?: emptyList()
    }

    inline fun <reified T> toJson(list: List<T>): String {
        val type = Types.newParameterizedType(List::class.java, T::class.java)
        val adapter = moshi.adapter<List<T>>(type)
        return adapter.toJson(list)
    }
}