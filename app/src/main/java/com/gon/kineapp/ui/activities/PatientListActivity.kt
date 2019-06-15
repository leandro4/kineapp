package com.gon.kineapp.ui.activities

import android.os.Bundle
import com.gon.kineapp.ui.fragments.BaseMvpFragment
import com.gon.kineapp.ui.fragments.PatientListFragment

class PatientListActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setToolbarTitle("Listado Clientes")
    }

    override fun getFragment(): BaseMvpFragment {
        return PatientListFragment()
    }
}
