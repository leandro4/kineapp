package com.gon.kineapp.ui.activities

import android.os.Bundle
import com.gon.kineapp.ui.fragments.BaseMvpFragment
import com.gon.kineapp.ui.fragments.LoginFragment

class LoginActivity: BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setToolbarTitle("Bienvenido!")
    }

    override fun getFragment(): BaseMvpFragment {
        return LoginFragment().apply { activityProgress = this@LoginActivity }
    }
}