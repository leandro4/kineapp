package com.gon.kineapp.ui.activities

import android.content.Intent
import android.os.Bundle
import com.gon.kineapp.ui.fragments.BaseMvpFragment
import com.gon.kineapp.ui.fragments.LoginFragment

class LoginActivity: BaseActivity() {

    private val frag = LoginFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setToolbarTitle("Bienvenido!")
    }

    override fun getFragment(): BaseMvpFragment {
        return frag.apply { activityProgress = this@LoginActivity }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        frag.onActivityResult(requestCode, resultCode, data)
    }

}