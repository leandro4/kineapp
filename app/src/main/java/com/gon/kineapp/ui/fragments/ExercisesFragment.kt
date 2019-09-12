package com.gon.kineapp.ui.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.gon.kineapp.R
import com.gon.kineapp.model.Routine
import com.gon.kineapp.ui.adapters.ExerciseAdapter
import kotlinx.android.synthetic.main.fragment_exercises.*

class ExercisesFragment : Fragment(), ExerciseAdapter.ExerciseListener {

    private lateinit var routine: Routine
    private var isMedic = false
    private lateinit var adapter: ExerciseAdapter

    companion object {
        fun newInstance(isMedic: Boolean, exercises: Routine): ExercisesFragment {
            val frag = ExercisesFragment()
            frag.isMedic = isMedic
            frag.routine = exercises
            return frag
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_exercises, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        rvExercises.layoutManager = LinearLayoutManager(context)
        adapter = ExerciseAdapter(routine.exercises, this, isMedic)
        rvExercises.adapter = adapter

        if (routine.exercises.isEmpty()) {
            emptyView.visibility = View.VISIBLE
        }
    }

    override fun onWatchVideo(url: String) {
        Toast.makeText(context, "watch", Toast.LENGTH_SHORT).show()
    }

    override fun onAddVideo(exerciseId: String) {
        Toast.makeText(context, "add", Toast.LENGTH_SHORT).show()
    }

    override fun onExerciseMarkedAsDone(exerciseId: String) {
        val index = routine.exercises.indexOfFirst { it.id == (exerciseId) }
        routine.exercises[index].done = true
        adapter.notifyItemChanged(index)
    }

    override fun onExerciseRemove(exerciseId: String) {
        Toast.makeText(context, "remove", Toast.LENGTH_SHORT).show()
    }
}