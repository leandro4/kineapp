package com.gon.kineapp.ui.activities

import com.gon.kineapp.model.Patient
import com.gon.kineapp.ui.fragments.BaseMvpFragment
import com.gon.kineapp.ui.fragments.PatientDetailFragment
import com.gon.kineapp.utils.Constants

class PatientDetailActivity : BaseActivity() {

    override fun getFragment(): BaseMvpFragment {
        val patient = intent?.getParcelableExtra<Patient>(Constants.PATIENT_EXTRA)
        return PatientDetailFragment.newInstance(patient!!)
    }
}
