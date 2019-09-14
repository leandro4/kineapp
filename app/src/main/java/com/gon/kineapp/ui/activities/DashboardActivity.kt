package com.gon.kineapp.ui.activities

import android.util.ArrayMap
import com.gon.kineapp.model.Exercise
import com.gon.kineapp.model.Routine
import com.gon.kineapp.ui.fragments.BaseMvpFragment
import com.gon.kineapp.ui.fragments.RoutineFragment
import com.gon.kineapp.ui.fragments.PatientsListFragment
import com.gon.kineapp.utils.MyUser

class DashboardActivity : BaseActivity() {

    override fun getFragment(): BaseMvpFragment {
        val myUser = MyUser.get(this)
        return if (myUser!!.isMedic()) PatientsListFragment() else {
            val exercises = ArrayMap<Int, MutableList<Exercise>>()

            val ex = Exercise("31431", "Cruzar la 9 de Julio de punta a punta con los ojos vendados de ida y vuelta. Repetir 3 veces.", false, null)
            val ex2 = Exercise("31432", "Repetir 15 series de 20 saltos de rana. Sin descansar. Esto es el regimiento.", false, null)
            val ex3 = Exercise("31433", "Correr 5 vueltas a la manzana, por el medio de la calle esquivando autos.", false, null)

            exercises[0] = listOf(ex2, ex3).toMutableList()
            exercises[1] = listOf(ex, ex2, ex3).toMutableList()
            exercises[2] = ArrayList()
            exercises[3] = listOf(ex3).toMutableList()
            exercises[4] = ArrayList()
            exercises[5] = listOf(ex, ex2, ex3).toMutableList()
            exercises[6] = ArrayList()

            val user = MyUser.get(this)?.apply { patient?.routine = exercises }
            MyUser.set(this, user)
            RoutineFragment.newInstance(false, user!!)
        }
    }

    override fun enabledBackButton(): Boolean {
        return false
    }
}