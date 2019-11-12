package com.gon.kineapp.ui.activities

import android.content.Intent
import android.content.pm.PackageManager
import com.gon.kineapp.model.Patient
import com.gon.kineapp.model.User
import com.gon.kineapp.ui.fragments.BaseMvpFragment
import com.gon.kineapp.ui.fragments.PatientDetailFragment
import com.gon.kineapp.utils.Constants

class PatientDetailActivity : BaseActivity() {

    private lateinit var fragment: PatientDetailFragment

    override fun getFragment(): BaseMvpFragment {
        val patient = intent?.getParcelableExtra<User>(Constants.PATIENT_EXTRA)
        fragment = PatientDetailFragment.newInstance(patient!!)
        return fragment
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        fragment.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.PERMISSIONS_REQUEST_CODE && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
            fragment.onPermissionsGranted()
        }
    }

    override fun onBackPressed() {
        val intent = Intent()
        intent.putExtra(Constants.USER_EXTRA, fragment.patient)
        setResult(Constants.EDITED_ROUTINE_CODE, intent)
        super.onBackPressed()
    }
}
