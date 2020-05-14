package com.memo.mynotes.utils

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

object MessageHandler {
    fun displayToast(ac: AppCompatActivity, msg: String){
        Toast.makeText(ac, msg, Toast.LENGTH_SHORT).show()
    }
}