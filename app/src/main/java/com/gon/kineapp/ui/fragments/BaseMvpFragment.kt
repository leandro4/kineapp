package com.gon.kineapp.ui.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AlertDialog
import android.view.View
import com.gon.kineapp.R
import com.gon.kineapp.mvp.views.BaseView
import com.gon.kineapp.ui.activities.SplashActivity
import com.gon.kineapp.utils.Authorization
import com.gon.kineapp.utils.MyUser
import com.gonanimationlib.animations.Animate
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

abstract class BaseMvpFragment: androidx.fragment.app.Fragment(), BaseView {

    var progressBar: View? = null
    var activityProgress: ActivityProgress? = null

    interface ActivityProgress {
        fun showProgress()
        fun hideProgress()
    }

    protected fun showErrorMessage(message: String) {
        context?.let {
            AlertDialog.Builder(it)
                    .setMessage(message)
                    .setPositiveButton(android.R.string.ok) { dialog, _ -> dialog.dismiss() }
                    .show()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar = view.findViewById(R.id.viewLoading)
        startPresenter()
    }

    abstract fun startPresenter()

    override fun getMvpContext(): Context? {
        return context
    }

    override fun onError(var1: Throwable) {
        showErrorMessage(var1.message?: run { getString(R.string.generic_error_message) })
    }

    override fun onNoInternetConnection() {
        showErrorMessage(getString(R.string.warning_no_conection))
    }

    override fun showProgressView() {
        progressBar?.let {
            it.visibility = View.VISIBLE
            Animate.ALPHA(1f).duration(Animate.DURATION_MEDIUM).startAnimation(progressBar)
        }
        activityProgress?.showProgress()
    }

    override fun hideProgressView() {
        progressBar?.let {
            Animate.ALPHA(0f).duration(Animate.DURATION_MEDIUM).onEnd { progressBar?.visibility = View.GONE }
                .startAnimation(progressBar)
        }
        activityProgress?.hideProgress()
    }

    protected fun alreadySignedInUser(): Boolean {
        return GoogleSignIn.getLastSignedInAccount(context) != null
    }

    protected fun getGoogleAccount(): GoogleSignInAccount? {
        return GoogleSignIn.getLastSignedInAccount(context)
    }

    open fun handleBackPressed(): Boolean {
        return false
    }

    protected fun logOut() {
        showProgressView()
        GoogleSignIn.getClient(activity!!, GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()).revokeAccess()
        GoogleSignIn.getClient(activity!!, GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()).signOut()
            .addOnCompleteListener(activity!!) {
                MyUser.set(context!!, null)
                Authorization.getInstance().set("")
                activity?.startActivity(Intent(context, SplashActivity::class.java))
                activity?.finish()
            }
    }

    override fun onErrorCode(message: String) {
        showErrorMessage(message)//getString(R.string.http_error_message))
    }

}