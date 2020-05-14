package com.memo.mynotes.ui.activities

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.memo.mynotes.R
import gr.net.maroulis.library.EasySplashScreen

class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val config = EasySplashScreen(this)
            .withFullScreen()
            .withTargetActivity(HomePage::class.java)
            .withSplashTimeOut(1000)
            .withBackgroundResource(R.color.colorPrimaryDark)
            .withLogo(R.drawable.ic_logo)

        val view = config.create()
        setContentView(view)
    }
}
