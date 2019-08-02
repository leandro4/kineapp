package com.gon.kineapp.ui.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.gon.kineapp.R

class SplashActivity : LockableActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
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
        Handler().postDelayed( {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }, 1500)
    }
}