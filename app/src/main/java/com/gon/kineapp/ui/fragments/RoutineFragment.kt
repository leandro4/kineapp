package com.gon.kineapp.ui.fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import com.gon.kineapp.R
import com.gon.kineapp.model.Exercise
import com.gon.kineapp.model.Routine
import com.gon.kineapp.model.User
import com.gon.kineapp.mvp.presenters.RoutinePresenter
import com.gon.kineapp.mvp.views.RoutineView
import com.gon.kineapp.ui.activities.CreateExerciseActivity
import com.gon.kineapp.ui.activities.ProfileActivity
import com.gon.kineapp.ui.adapters.RoutinePagerAdapter
import com.gon.kineapp.utils.Constants
import com.gon.kineapp.utils.MyUser
import kotlinx.android.synthetic.main.fragment_exercise_routines.*
import kotlinx.android.synthetic.main.fragment_exercises.*

class RoutineFragment: BaseMvpFragment(), RoutineView, ExercisesFragment.ExerciseListener {

    private val presenter = RoutinePresenter()
    private lateinit var adapter: RoutinePagerAdapter
    private var isMedic = false
    private lateinit var user: User

    companion object {
        fun newInstance(isMedic: Boolean, user: User): RoutineFragment {
            val frag = RoutineFragment()
            frag.isMedic = isMedic
            frag.user = user
            return frag
        }

        const val CREATE_EXERCISE_CODE = 3000
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(com.gon.kineapp.R.layout.fragment_exercise_routines, container, false)
        setHasOptionsMenu(true)
        return v
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (!isMedic) {
            inflater.inflate(R.menu.menu_patients_home, menu)
        }
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        adapter = RoutinePagerAdapter(fragmentManager!!)
        user.patient?.routine?.values?.forEach {
            adapter.addFragment(ExercisesFragment.newInstance(isMedic, Routine(it), this@RoutineFragment))
        }
        vpRoutines.offscreenPageLimit = 7
        vpRoutines.adapter = adapter

        tabLayout.setupWithViewPager(vpRoutines)

        if (!isMedic) {
            fabAddExercise.hide()
        } else {
            fabAddExercise.setOnClickListener {
                activity?.startActivityForResult(Intent(activity, CreateExerciseActivity::class.java), CREATE_EXERCISE_CODE)
            }
        }
    }

    override fun startPresenter() {
        presenter.attachMvpView(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachMvpView()
    }

    override fun onExercisesEdited(ex: Exercise) {
        if (!isMedic) {
            val user = MyUser.get(context!!)
            user?.patient?.routine?.get(ex.day)?.let {
                val found = it.find { e -> e.id == ex.id }
                found?.done = ex.done
                found?.name = ex.name
                found?.description = ex.description
                found?.video = ex.video
            }
            MyUser.set(context!!, user)
        }
    }

    override fun onExercisesCreated(exercises: MutableList<Exercise>) {
        exercises.forEach { adapter.fragments[it.day].add(it) }
    }

    override fun onExerciseDeleted() {}

    override fun onMarkAsDoneVideo(exercise: Exercise) {
        presenter.markAsDoneExercise(exercise.id)
    }

    override fun onRemoveExercise(exercise: Exercise) {
        presenter.deleteExercise(exercise.id)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CREATE_EXERCISE_CODE && resultCode == RESULT_OK) {
            data?.let {
                val title = it.getStringExtra(Constants.EXERCISE_TITLE_EXTRA)
                val description = it.getStringExtra(Constants.EXERCISE_DESCRIPTION_EXTRA)
                val days = it.getIntegerArrayListExtra(Constants.EXERCISE_DAYS_EXTRA)
                val videoId = it.getStringExtra(Constants.EXERCISE_VIDEO_ID_EXTRA)

                presenter.createExercise(user.id, title, description, videoId, days)
            }
        }
    }
}