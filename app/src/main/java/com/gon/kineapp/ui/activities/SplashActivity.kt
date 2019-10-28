package com.gon.kineapp.ui.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.gon.kineapp.R

class SplashActivity : LockableActivity() {

    private val handler = Handler()
    private var runnable: Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        runnable = Runnable {
            startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
            finish()
        }
    }

    override fun canNavigateToSignIn(): Boolean {
        return false
    }

    override fun onStart() {
        super.onStart()
        if (!alreadySignedInUser()) {
            goToLogin()
        } else {
            goToHome()
        }
    }

    override fun onUnlockedScreen() {
        goToHome()
    }

    private fun goToHome() {
        startActivity(Intent(this, DashboardActivity::class.java))
        finish()
    }

    private fun goToLogin() {
        handler.postDelayed(runnable, 1500)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        handler.removeCallbacks(runnable)
    }
}