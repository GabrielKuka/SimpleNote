package com.memo.mynotes.repositories

import android.app.Application
import android.content.Context
import com.memo.mynotes.utils.AppData

class SharedPrefsRepo(application: Application) {

    private val sharedPrefs = application.getSharedPreferences("Data", Context.MODE_PRIVATE)

    private val layoutPref: Int = sharedPrefs.getInt("layoutKey", AppData.LINEAR_LAYOUT)

    fun setLayoutPref(value: Int) {
        with(sharedPrefs.edit()) {
            putInt("layoutKey", value)
            apply()
        }
    }

    fun getLayoutPref(): Int {
        return layoutPref
    }

}