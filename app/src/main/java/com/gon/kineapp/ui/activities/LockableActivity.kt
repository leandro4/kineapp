package com.gon.kineapp.ui.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import com.gon.kineapp.ui.fragments.dialogs.UnlockerQuestionFragment
import com.gon.kineapp.utils.LockerAppCallback
import com.google.android.gms.auth.api.signin.GoogleSignIn

abstract class LockableActivity : AppCompatActivity() {

    override fun onResume() {
        super.onResume()
        if (!alreadySignedInUser()) {
            if (canNavigateToSignIn()) {
                startActivity(Intent(this, SplashActivity::class.java))
                finish()
            }
        } else if (LockerAppCallback.TimerSessionClient.lockedApp(this)) {
            showSecretQuestion()
        }
    }

    protected fun alreadySignedInUser(): Boolean {
        return GoogleSignIn.getLastSignedInAccount(this) != null
    }

    protected open fun canNavigateToSignIn(): Boolean {
        return true
    }

    private fun showSecretQuestion() {
        UnlockerQuestionFragment()
            .setListener(object : UnlockerQuestionFragment.ResponseListener {
                override fun onSuccessResponse() {
                    onUnlockedScreen()
                }
            })
            .show(supportFragmentManager, "question")
    }

    protected open fun onUnlockedScreen() {}
}
