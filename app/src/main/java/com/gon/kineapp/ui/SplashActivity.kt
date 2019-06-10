package com.gon.kineapp.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import com.gon.kineapp.R
import com.gon.kineapp.ui.activities.LoginActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }

    override fun onStart() {
        super.onStart()
        Handler().postDelayed( {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }, 1500)
    }
}