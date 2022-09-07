package com.naulian.anhance

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "preferences")

@Suppress("MemberVisibilityCanBePrivate", "unused")
class AnStore(val context: Context) {

    suspend fun readBoolean(key: String): Boolean {
        return booleanPreferenceFlow(key).first()
    }

    suspend fun writeBoolean(key: String, value: Boolean) {
        context.dataStore.edit {
            it[booleanPreferencesKey(key)] = value
        }
    }

    suspend fun readInt(key: String, defValue: Int): Int {
        return intPreferenceFlow(key, defValue).first()
    }

    suspend fun writeInt(key: String, value: Int) {
        context.dataStore.edit {
            it[intPreferencesKey(key)] = value
        }
    }

    suspend fun readString(key: String, defValue: String): String {
        return stringPreferenceFlow(key, defValue).first()
    }

    suspend fun writeString(key: String, value: String) {
        context.dataStore.edit {
            it[stringPreferencesKey(key)] = value
        }
    }

    suspend fun readLong(key: String, defValue: Long): Long {
        return longPreferenceFlow(key, defValue).first()
    }

    suspend fun writeLong(key: String, value: Long) {
        context.dataStore.edit {
            it[longPreferencesKey(key)] = value
        }
    }

    fun stringPreferenceFlow(key: String, defValue: String): Flow<String> {
        return context.dataStore.data.map {
            it[stringPreferencesKey(key)] ?: defValue
        }
    }

    fun booleanPreferenceFlow(key: String): Flow<Boolean> {
        return context.dataStore.data.map {
            it[booleanPreferencesKey(key)] ?: true
        }
    }

    fun intPreferenceFlow(key: String, defValue: Int) =
        context.dataStore.data.map {
            it[intPreferencesKey(key)] ?: defValue
        }

    fun longPreferenceFlow(key: String, defValue: Long) =
        context.dataStore.data.map {
            it[longPreferencesKey(key)] ?: defValue
        }
}