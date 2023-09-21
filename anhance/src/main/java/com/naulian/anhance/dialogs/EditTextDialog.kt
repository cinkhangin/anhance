@file:Suppress("unused")

package com.naulian.anhance.dialogs

import android.content.Context
import android.view.LayoutInflater
import com.naulian.anhance.databinding.DialogEditTextBinding
import com.naulian.anhance.dialogBuilder
import com.naulian.anhance.setPrimaryButton
import com.naulian.anhance.setSecondaryButton
import com.naulian.anhance.textTrim

class EditTextDialog(context: Context) {
    private val dialog = dialogBuilder(context)

    private val inflater = LayoutInflater.from(context)
    private val binding = DialogEditTextBinding.inflate(inflater)

    init {
        dialog.setView(binding.root)
    }

    fun setTitle(title: String): EditTextDialog {
        dialog.setTitle(title)
        return this
    }

    fun setMessage(message: String): EditTextDialog {
        dialog.setMessage(message)
        return this
    }


    fun onPrimaryAction(text: String, action: (String) -> Unit): EditTextDialog {
        dialog.setPrimaryButton(text) {
            action(binding.editMessage.textTrim())
        }
        return this
    }

    fun onSecondaryAction(text: String, action: () -> Unit): EditTextDialog {
        dialog.setSecondaryButton(text) { action() }
        return this
    }


    fun show() {
        dialog.show()
    }
}