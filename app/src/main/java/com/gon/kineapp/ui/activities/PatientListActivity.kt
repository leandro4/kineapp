package com.gon.kineapp.ui.activities

import android.os.Bundle
import com.gon.kineapp.R
import com.gon.kineapp.ui.fragments.BaseMvpFragment
import com.gon.kineapp.ui.fragments.PatientListFragment

class PatientListActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setToolbarTitle(getString(R.string.patients_list_title))
    }

    override fun getFragment(): BaseMvpFragment {
        return PatientListFragment()
    }

    override fun enabledBackButton(): Boolean {
        return false
    }
}
