package com.memo.mynotes.ui.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.memo.mynotes.R


class BasicDialog(private val content: String = "", private val action: () -> Unit) :
    DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage(content)
                .setPositiveButton(
                    R.string.yes
                ) { _, _ ->
                    // user clicks yes
                    action()
                }
                .setNegativeButton(
                    R.string.no
                ) { _, _ ->
                    // User cancelled the dialog
                }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}