@file:Suppress("unused")

package com.naulian.anhance

import android.content.DialogInterface
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun MaterialAlertDialogBuilder.setPrimaryButton(
    text: String, action: DialogInterface.() -> Unit
) = setPositiveButton(text) { dialog, _ -> action(dialog) }

fun MaterialAlertDialogBuilder.setSecondaryButton(
    text: String, action: DialogInterface.() -> Unit
) = setNegativeButton(text) { dialog, _ -> action(dialog) }

fun MaterialAlertDialogBuilder.setTertiaryButton(
    text: String, action: DialogInterface.() -> Unit
) = setNeutralButton(text) { dialog, _ -> action(dialog) }