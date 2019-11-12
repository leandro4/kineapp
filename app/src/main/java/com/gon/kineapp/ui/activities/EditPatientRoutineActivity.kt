package com.gon.kineapp.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.ArrayMap
import com.gon.kineapp.R
import com.gon.kineapp.model.Exercise
import com.gon.kineapp.model.Routine
import com.gon.kineapp.model.User
import com.gon.kineapp.ui.fragments.BaseMvpFragment
import com.gon.kineapp.ui.fragments.RoutineFragment
import com.gon.kineapp.ui.fragments.PatientsListFragment
import com.gon.kineapp.utils.Constants
import com.gon.kineapp.utils.MyUser

class EditPatientRoutineActivity : BaseActivity() {

    private lateinit var fragment: RoutineFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setToolbarTitle(getString(R.string.routines_title))
    }

    override fun getFragment(): BaseMvpFragment {
        val patient = intent.getParcelableExtra<User>(Constants.USER_EXTRA)
        fragment = RoutineFragment.newInstance(true, patient)
        return fragment
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        fragment.onActivityResult(requestCode, resultCode, data)
    }

    override fun onBackPressed() {
        val intent = Intent()
        intent.putExtra(Constants.USER_EXTRA, fragment.user)
        setResult(Constants.EDITED_ROUTINE_CODE, intent)
        super.onBackPressed()
    }
}