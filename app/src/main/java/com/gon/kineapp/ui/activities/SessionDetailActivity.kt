package com.gon.kineapp.ui.activities

import android.content.Intent
import com.gon.kineapp.model.Session
import com.gon.kineapp.ui.fragments.BaseMvpFragment
import com.gon.kineapp.ui.fragments.SessionDetailFragment
import com.gon.kineapp.utils.Constants

class SessionDetailActivity : BaseActivity() {

    private lateinit var fragment: SessionDetailFragment

    override fun getFragment(): BaseMvpFragment {
        val session = intent?.getParcelableExtra<Session>(Constants.SESSION_EXTRA)
        fragment = SessionDetailFragment.newInstance(session!!)
        return fragment
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        fragment.onActivityResult(requestCode, resultCode, data)
    }
}
