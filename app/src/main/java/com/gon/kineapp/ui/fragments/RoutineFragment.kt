package com.gon.kineapp.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import com.gon.kineapp.R
import com.gon.kineapp.model.User
import com.gon.kineapp.mvp.presenters.RoutinePresenter
import com.gon.kineapp.mvp.views.RoutineView
import com.gon.kineapp.ui.activities.ProfileActivity
import com.gon.kineapp.ui.adapters.RoutinePagerAdapter
import kotlinx.android.synthetic.main.fragment_exercise_routines.*

class RoutineFragment: BaseMvpFragment(), RoutineView {

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
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(com.gon.kineapp.R.layout.fragment_exercise_routines, container, false)
        setHasOptionsMenu(true)
        return v
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_patients_home, menu)
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
            adapter.addFragment(ExercisesFragment.newInstance(isMedic, it))
        }
        vpRoutines.offscreenPageLimit = 7
        vpRoutines.adapter = adapter

        tabLayout.setupWithViewPager(vpRoutines)
    }

    override fun startPresenter() {
        presenter.attachMvpView(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachMvpView()
    }

    override fun onExercisesEdited() {
    }
}