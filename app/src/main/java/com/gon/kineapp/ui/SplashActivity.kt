package com.gon.kineapp.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import com.gon.kineapp.R
import com.gon.kineapp.ui.activities.MainActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        Handler().postDelayed( {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 1500)
    }
}