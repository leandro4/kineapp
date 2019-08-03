package com.gon.kineapp.ui.activities

import com.gon.kineapp.model.User
import com.gon.kineapp.ui.fragments.BaseMvpFragment
import com.gon.kineapp.ui.fragments.PrivateVideosListFragment
import com.gon.kineapp.utils.Constants

class PrivateVideosActivity : BaseActivity() {

    override fun getFragment(): BaseMvpFragment {
        val patient = intent.getParcelableExtra<User>(Constants.PATIENT_EXTRA)
        return PrivateVideosListFragment.newInstance(patient)
    }
}
