package com.gon.kineapp.ui.activities

import android.os.Bundle
import com.gon.kineapp.R
import com.gon.kineapp.ui.fragments.BaseMvpFragment
import com.gon.kineapp.ui.fragments.ProfileFragment

class ProfileActivity: BaseActivity() {

    private val frag = ProfileFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setToolbarTitle(getString(R.string.profile_title))
    }

    override fun getFragment(): BaseMvpFragment {
        return frag
    }
}