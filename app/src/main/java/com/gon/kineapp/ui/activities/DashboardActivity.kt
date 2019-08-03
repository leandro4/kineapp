package com.gon.kineapp.ui.activities

import com.gon.kineapp.ui.fragments.BaseMvpFragment
import com.gon.kineapp.ui.fragments.ExerciseRoutineFragment
import com.gon.kineapp.ui.fragments.PatientsListFragment
import com.gon.kineapp.utils.MyUser

class DashboardActivity : BaseActivity() {

    override fun getFragment(): BaseMvpFragment {
        val myUser = MyUser.get(this)
        return if (myUser!!.isMedic()) PatientsListFragment() else ExerciseRoutineFragment()
    }

    override fun enabledBackButton(): Boolean {
        return false
    }
}
