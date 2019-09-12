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
            val exercises = ArrayMap<Int, Routine>()

            val ex = Exercise("31431", "Cruzar la 9 de Julio de punta a punta con los ojos vendados de ida y vuelta. Repetir 3 veces.", false, null)
            val ex2 = Exercise("31432", "Repetir 15 series de 20 saltos de rana. Sin descansar. Esto es el regimiento.", false, null)
            val ex3 = Exercise("31433", "Correr 5 vueltas a la manzana, por el medio de la calle esquivando autos.", false, null)

            exercises[0] = Routine(listOf(ex2, ex3))
            exercises[1] = Routine(listOf(ex, ex2, ex3))
            exercises[2] = Routine(ArrayList())
            exercises[3] = Routine(listOf(ex3))
            exercises[4] = Routine(ArrayList())
            exercises[5] = Routine(listOf(ex, ex2, ex3))
            exercises[6] = Routine(ArrayList())

            val user = MyUser.get(this)?.apply { patient?.routine = exercises }
            MyUser.set(this, user)
            RoutineFragment.newInstance(false, user!!)
        }
    }

    override fun enabledBackButton(): Boolean {
        return false
    }
}