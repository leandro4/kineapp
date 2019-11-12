package com.gon.kineapp.ui.activities

import android.content.Intent
import com.gon.kineapp.model.User
import com.gon.kineapp.ui.fragments.BaseMvpFragment
import com.gon.kineapp.ui.fragments.PatientDetailFragment
import com.gon.kineapp.ui.fragments.RoutineFragment
import com.gon.kineapp.ui.fragments.PatientsListFragment
import com.gon.kineapp.utils.Constants
import com.gon.kineapp.utils.MyUser

class DashboardActivity : BaseActivity() {

    companion object {
        const val EDIT_ROUTINE = 8001
    }

    private lateinit var fragment: BaseMvpFragment

    override fun getFragment(): BaseMvpFragment {
        val myUser = MyUser.get(this)
        fragment = if (myUser!!.isMedic()) PatientsListFragment() else RoutineFragment.newInstance(false, myUser)
        return fragment
    }

    override fun enabledBackButton(): Boolean {
        return false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == EDIT_ROUTINE && resultCode == Constants.EDITED_ROUTINE_CODE && MyUser.get(this)!!.isMedic()) {
            data?.let {
                val user = it.getParcelableExtra<User>(Constants.USER_EXTRA)
                (fragment as PatientsListFragment).updateRoutineOf(user)
            }
        }
    }
}