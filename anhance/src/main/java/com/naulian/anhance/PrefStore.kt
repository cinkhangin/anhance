@file:Suppress("unused")

package com.naulian.anhance

import android.content.Context
import android.content.SharedPreferences

class PrefStore(context: Context, name: String = "pref_store") {
    private var sharedPref: SharedPreferences

    init {
        sharedPref = context.getSharedPreferences(name, Context.MODE_PRIVATE)
    }

    fun readInt(key: String, defValue: Int = 0): Int {
        return sharedPref.getInt(key, defValue)
    }

    fun writeInt(key: String, value: Int) {
        val editor = sharedPref.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    fun readString(key: String, defValue: String = ""): String {
        return sharedPref.getString(key, defValue) ?: defValue
    }

    fun writeString(key: String, value: String) {
        val editor = sharedPref.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun readFloat(key: String, defValue: Float = 0f): Float {
        return sharedPref.getFloat(key, defValue)
    }

    fun writeFloat(key: String, value: Float) {
        val editor = sharedPref.edit()
        editor.putFloat(key, value)
        editor.apply()
    }

    fun readLong(key: String, defValue: Long = 0L): Long {
        return sharedPref.getLong(key, defValue)
    }

    fun writeLong(key: String, value: Long) {
        val editor = sharedPref.edit()
        editor.putLong(key, value)
        editor.apply()
    }

    fun readBoolean(key: String, defValue: Boolean = false): Boolean {
        return sharedPref.getBoolean(key, defValue)
    }

    fun writeBoolean(key: String, value: Boolean) {
        val editor = sharedPref.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }
}