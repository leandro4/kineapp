package com.gon.kineapp.ui.activities

import android.content.Intent
import android.os.Bundle
import com.gon.kineapp.ui.fragments.BaseMvpFragment
import com.gon.kineapp.ui.fragments.LoginFragment
import com.gon.kineapp.ui.fragments.ProfileFragment

class ProfileActivity: BaseActivity() {

    private val frag = ProfileFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setToolbarTitle("Perfil")
    }

    override fun canNavigateToSignIn(): Boolean {
        return false
    }

    override fun getFragment(): BaseMvpFragment {
        return frag.apply { activityProgress = this@ProfileActivity }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        frag.onActivityResult(requestCode, resultCode, data)
    }

}