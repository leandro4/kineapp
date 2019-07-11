package com.gon.kineapp.ui.activities

import com.gon.kineapp.model.Session
import com.gon.kineapp.ui.fragments.BaseMvpFragment
import com.gon.kineapp.ui.fragments.SessionDetailFragment
import com.gon.kineapp.utils.Constants

class SessionDetailActivity : BaseActivity() {

    override fun getFragment(): BaseMvpFragment {
        val session = intent?.getParcelableExtra<Session>(Constants.SESSION_EXTRA)
        return SessionDetailFragment.newInstance(session!!)
    }
}
