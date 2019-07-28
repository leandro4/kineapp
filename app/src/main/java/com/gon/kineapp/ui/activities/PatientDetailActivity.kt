package com.gon.kineapp.ui.activities

import android.content.Intent
import com.gon.kineapp.model.Patient
import com.gon.kineapp.ui.fragments.BaseMvpFragment
import com.gon.kineapp.ui.fragments.PatientDetailFragment
import com.gon.kineapp.utils.Constants

class PatientDetailActivity : BaseActivity() {

    private lateinit var fragment: PatientDetailFragment

    override fun getFragment(): BaseMvpFragment {
        val patient = intent?.getParcelableExtra<Patient>(Constants.PATIENT_EXTRA)
        fragment = PatientDetailFragment.newInstance(patient!!)
        return fragment
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        fragment.onActivityResult(requestCode, resultCode, data)
    }
}
