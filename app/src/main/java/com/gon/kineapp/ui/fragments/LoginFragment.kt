package com.gon.kineapp.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.gon.kineapp.R
import com.gon.kineapp.mvp.presenters.LoginPresenter
import com.gon.kineapp.mvp.views.LoginView
import com.gon.kineapp.ui.activities.PatientListActivity
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment: BaseMvpFragment(), LoginView {

    var presenter: LoginPresenter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(context).inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startPresenter()
        initUI()
    }

    private fun initUI() {
        fabLogin.setOnClickListener {
            activity?.startActivity(Intent(activity, PatientListActivity::class.java))
            activity?.finish()
        }
    }

    override fun startPresenter() {
        presenter?.attachMvpView(this)
    }

    override fun onErrorCode(message: String) {
        showErrorMessage(getString(R.string.generic_error_message))
    }

    override fun onDestroy() {
        presenter?.detachMvpView()
        super.onDestroy()
    }

    override fun onLoginSuccess() {

    }

    override fun onLoginFailure() {

    }
}