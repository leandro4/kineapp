package com.gon.kineapp.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import com.gon.kineapp.R
import com.gon.kineapp.model.ExercisesCalendar
import com.gon.kineapp.mvp.presenters.ExercisesPresenter
import com.gon.kineapp.mvp.views.ExercisesView
import com.gon.kineapp.ui.activities.ProfileActivity

class ExerciseRoutineFragment: BaseMvpFragment(), ExercisesView {

    private val presenter = ExercisesPresenter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(com.gon.kineapp.R.layout.fragment_exercise_routines, container, false)
        setHasOptionsMenu(true)
        return v
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_home, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {
            R.id.profile -> {
                activity?.startActivity(Intent(context, ProfileActivity::class.java))
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun startPresenter() {
        presenter.attachMvpView(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachMvpView()
    }

    override fun onExercisesResponse(exercises: MutableList<ExercisesCalendar>) {
    }

    override fun onExercisesEdited() {
    }
}