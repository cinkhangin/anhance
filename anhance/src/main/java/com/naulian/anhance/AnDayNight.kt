@file:Suppress("unused")

package com.naulian.anhance

import android.content.Context
import android.content.res.Configuration
import com.naulian.anhance.objects.AnTheme

val Context.configuration: Configuration get() = this.resources.configuration
val Context.uiMode get() = this.configuration.uiMode

fun runLightTheme() = AnTheme.setTheme(AnTheme.LIGHT_MODE)
fun runNightTheme() = AnTheme.setTheme(AnTheme.NIGHT_MODE)
fun runSystemTheme() = AnTheme.setTheme(AnTheme.SYSTEM_MODE)
fun getCurrentTheme() = AnTheme.currentTheme

suspend fun Context.loadTheme() = AnTheme.loadTheme(this)
suspend fun Context.installLightTheme() =
    AnTheme.setAndSaveTheme(this, AnTheme.LIGHT_MODE)

suspend fun Context.installNightTheme() =
    AnTheme.setAndSaveTheme(this, AnTheme.NIGHT_MODE)

suspend fun Context.installSystemTheme() =
    AnTheme.setAndSaveTheme(this, AnTheme.SYSTEM_MODE)

fun Context.isNightMode() = AnTheme.isNightMode(this)
fun Context.isLightMode() = AnTheme.isLightMode(this)