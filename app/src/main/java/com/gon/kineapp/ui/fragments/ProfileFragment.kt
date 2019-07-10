package com.gon.kineapp.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.gon.kineapp.R
import com.gon.kineapp.mvp.presenters.LoginPresenter
import com.gon.kineapp.mvp.views.LoginView
import com.gon.kineapp.ui.activities.PatientListActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.ApiException
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_login.etPhone
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment: BaseMvpFragment(), LoginView {

    var presenter = LoginPresenter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(context).inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startPresenter()
        initUI()
    }

    private fun initUI() {


        Glide.with(context!!).load(Uri.parse("https://www.elpopular.pe/sites/default/files/styles/img_620x465/public/imagen/2018/10/21/Noticia-218698-hombre-se-llevo-sorpresa-al-descubrir-que-su-perrito-era-un-animal-de-otra-especie.jpg?itok=3a9Eb3oL"))
            .into(civAvatar)

    }

    override fun startPresenter() {
        presenter.attachMvpView(this)
    }

    override fun onErrorCode(message: String) {
        showErrorMessage(getString(R.string.generic_error_message))
    }

    override fun onDestroy() {
        presenter.detachMvpView()
        super.onDestroy()
    }

    override fun onLoginSuccess() {
    }

    override fun onLoginFailure() {
        showErrorMessage(getString(R.string.generic_error_message))
    }
}