package com.gon.kineapp.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import com.gon.kineapp.R
import com.gon.kineapp.ui.activities.LoginActivity
import com.gon.kineapp.ui.fragments.SecreteQuestionFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount

class SplashActivity : AppCompatActivity() {

    private var account: GoogleSignInAccount? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        checkAlreadySignedInUser()
    }

    override fun onStart() {
        super.onStart()
        account?.let {
            showSecretQuestion()
        } ?: run {
            Handler().postDelayed( {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }, 1500)
        }
    }

    private fun checkAlreadySignedInUser() {
        account = GoogleSignIn.getLastSignedInAccount(this)
    }

    private fun showSecretQuestion() {
        SecreteQuestionFragment()
            .setListener(object : SecreteQuestionFragment.ResponseListener {
                override fun onSuccessResponse() {
                    startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                    finish()
                }
            })
            .show(supportFragmentManager, "question")
    }
}