package com.gon.kineapp.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.gon.kineapp.R
import com.gon.kineapp.model.Exercise
import com.gon.kineapp.model.Routine
import com.gon.kineapp.model.Video
import com.gon.kineapp.ui.activities.ViewVideoActivity
import com.gon.kineapp.ui.adapters.ExerciseAdapter
import com.gon.kineapp.utils.Constants
import kotlinx.android.synthetic.main.fragment_exercises.*

class ExercisesFragment : androidx.fragment.app.Fragment(), ExerciseAdapter.ExerciseListener {

    private lateinit var routine: Routine
    private var isMedic = false
    private lateinit var adapter: ExerciseAdapter
    private lateinit var listener: ExerciseListener

    interface ExerciseListener {
        fun onMarkAsDone(exercise: Exercise)
        fun onRemoveExercise(exercise: Exercise)
        fun onRefreshRoutine()
    }

    companion object {
        fun newInstance(isMedic: Boolean, exercises: Routine, listener: ExerciseListener): ExercisesFragment {
            val frag = ExercisesFragment()
            frag.isMedic = isMedic
            frag.routine = exercises
            frag.listener = listener
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
        swipeRefresh.setOnRefreshListener {
            listener.onRefreshRoutine()
        }

        swipeRefresh.isEnabled = !isMedic

        rvExercises.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
        adapter = ExerciseAdapter(routine.exercises, this, isMedic)
        rvExercises.adapter = adapter

        if (isMedic) {
            emptyViewIcon.setImageResource(R.drawable.ic_excercises)
            emptyViewTitle.setText(R.string.routine_empty_add_new)
        }
        checkEmptyList()
    }

    override fun onWatchVideo(video: Video) {
        val intent = Intent(activity, ViewVideoActivity::class.java)
        intent.putExtra(Constants.VIDEO_EXTRA, video)
        activity?.startActivity(intent)
    }

    override fun onExerciseMarkedAsDone(exercise: Exercise) {
        val index = routine.exercises.indexOfFirst { it.id == (exercise.id) }
        routine.exercises[index].done = true
        adapter.notifyItemChanged(index)

        listener.onMarkAsDone(exercise)
    }

    override fun onExerciseRemove(exercise: Exercise) {
        listener.onRemoveExercise(exercise)
    }

    fun onExerciseRemoved(id: String) {
        val index = routine.exercises.indexOfFirst { it.id == (id) }
        if (index == -1) return
        routine.exercises.removeAt(index)
        adapter.notifyItemRemoved(index)
        checkEmptyList()
    }

    private fun checkEmptyList() {
        if (routine.exercises.isEmpty()) {
            emptyView.visibility = View.VISIBLE
        } else {
            emptyView.visibility = View.GONE
        }
    }

    fun add(exercise: Exercise) {
        adapter.addExercise(exercise)
        checkEmptyList()
    }

    fun updateExercises(newExercises: MutableList<Exercise>) {
        routine.exercises.clear()
        routine.exercises.addAll(newExercises)
        adapter.updateExercises()
        checkEmptyList()
        swipeRefresh.isRefreshing = false
    }
}