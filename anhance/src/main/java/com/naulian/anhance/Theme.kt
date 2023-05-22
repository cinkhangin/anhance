@file:Suppress("unused")

package com.naulian.anhance

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import kotlinx.coroutines.*

val Context.configuration: Configuration get() = this.resources.configuration
val Context.uiMode get() = this.configuration.uiMode

//change or set
fun runLightTheme() = AnTheme.setTheme(AnTheme.LIGHT_MODE)
fun runDayTheme() = AnTheme.setTheme(AnTheme.LIGHT_MODE)
fun runNightTheme() = AnTheme.setTheme(AnTheme.NIGHT_MODE)
fun runDarkTheme() = AnTheme.setTheme(AnTheme.NIGHT_MODE)

fun runSystemTheme() = AnTheme.setTheme(AnTheme.SYSTEM_MODE)
fun getCurrentTheme() = AnTheme.currentTheme

fun loadThemeOnCreate(context: Context) {
    //Experimental
    runBlocking { context.loadTheme() }
}

//change or set then save
suspend fun Context.loadTheme() = AnTheme.loadTheme(this)
suspend fun Context.installLightTheme() =
    AnTheme.setAndSaveTheme(this, AnTheme.LIGHT_MODE)

suspend fun Context.installDayTheme() = installLightTheme()

suspend fun Context.installNightTheme() =
    AnTheme.setAndSaveTheme(this, AnTheme.NIGHT_MODE)

suspend fun Context.installDarkTheme() = installNightTheme()

suspend fun Context.installSystemTheme() =
    AnTheme.setAndSaveTheme(this, AnTheme.SYSTEM_MODE)

fun Context.isNightMode() = AnTheme.isNightMode(this)
fun Context.isLightMode() = AnTheme.isLightMode(this)

object AnTheme {
    const val SYSTEM_MODE = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
    const val NIGHT_MODE = AppCompatDelegate.MODE_NIGHT_YES
    const val LIGHT_MODE = AppCompatDelegate.MODE_NIGHT_NO

    private const val NIGHT_MASK = Configuration.UI_MODE_NIGHT_MASK
    private const val NIGHT_YES = Configuration.UI_MODE_NIGHT_YES
    private const val NIGHT_NO = Configuration.UI_MODE_NIGHT_NO

    private const val themeKey = "theme"

    var currentTheme = SYSTEM_MODE
        private set

    fun setTheme(mode: Int) {
        currentTheme = mode
        AppCompatDelegate.setDefaultNightMode(mode)
    }

    suspend fun loadTheme(context: Context) {
        setTheme(readTheme(context))
    }

    suspend fun setAndSaveTheme(context: Context, mode: Int) {
        setTheme(mode)
        saveTheme(context, mode)
    }

    private suspend fun saveTheme(context: Context, mode: Int) {
        context.writeInt(themeKey, mode)
    }

    private suspend fun readTheme(context: Context): Int {
        return context.readInt(themeKey, SYSTEM_MODE)
    }

    fun isNightMode(context: Context) =
        context.uiMode and NIGHT_MASK == NIGHT_YES

    fun isLightMode(context: Context) =
        context.uiMode and NIGHT_MASK == NIGHT_NO
}