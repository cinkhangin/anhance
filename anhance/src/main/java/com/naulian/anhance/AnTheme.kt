package com.naulian.anhance

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate

object AnTheme {
    const val SYSTEM_MODE = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
    const val NIGHT_MODE = AppCompatDelegate.MODE_NIGHT_YES
    const val LIGHT_MODE = AppCompatDelegate.MODE_NIGHT_NO

    const val NIGHT_MASK = Configuration.UI_MODE_NIGHT_MASK
    const val NIGHT_YES = Configuration.UI_MODE_NIGHT_YES
    const val NIGHT_NO = Configuration.UI_MODE_NIGHT_NO

    private const val themeKey = "theme"

    var currentTheme = SYSTEM_MODE
        private set

    fun setTheme(mode: Int) {
        currentTheme = mode
        AppCompatDelegate.setDefaultNightMode(mode)
    }

    suspend fun loadTheme(context: Context){
        setTheme(readTheme(context))
    }

    suspend fun setAndSaveTheme(context: Context, mode: Int){
        setTheme(mode)
        saveTheme(context, mode)
    }

    suspend fun saveTheme(context: Context, mode: Int){
        context.writeInt(themeKey , mode)
    }

    suspend fun readTheme(context: Context) : Int{
        return context.readInt(themeKey, SYSTEM_MODE)
    }

    fun isNightMode(context: Context) =
        context.uiMode and NIGHT_MASK == NIGHT_YES

    fun isLightMode(context: Context) =
        context.uiMode and NIGHT_MASK == NIGHT_NO
}