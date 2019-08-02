package com.gon.kineapp.ui.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import com.gon.kineapp.ui.fragments.dialogs.UnlockerQuestionFragment
import com.gon.kineapp.utils.LockerAppCallback
import com.gon.kineapp.utils.MyUser
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

abstract class LockableActivity : AppCompatActivity() {

    override fun onResume() {
        super.onResume()
        if (isLockable()) {
            if (!alreadySignedInUser()) {
                if (canNavigateToSignIn()) {
                    startActivity(Intent(this, SplashActivity::class.java))
                    finish()
                }
            } else if (LockerAppCallback.TimerSessionClient.lockedApp(this)) {
                showSecretQuestion()
            }
        }
    }

    protected open fun isLockable(): Boolean {
        return true
    }

    protected fun alreadySignedInUser(): Boolean {
        val googleLogged =  GoogleSignIn.getLastSignedInAccount(this) != null
        val localLogged = MyUser.getMyUser(this) != null
        if (!localLogged && googleLogged) {
            logOut()
            return false
        } else if (!localLogged || !googleLogged) {
            MyUser.setMyUser(this, null)
            return false
        }
        return true
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

    protected fun logOut() {
        GoogleSignIn.getClient(this, GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()).revokeAccess()
        GoogleSignIn.getClient(this, GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()).signOut()
            .addOnCompleteListener(this) {
                startActivity(Intent(this, SplashActivity::class.java))
                finish()
            }
    }

    protected open fun onUnlockedScreen() {}
}
