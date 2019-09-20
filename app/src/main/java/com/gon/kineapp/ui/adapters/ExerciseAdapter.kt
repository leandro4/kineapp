package com.gon.kineapp.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gon.kineapp.R
import com.gon.kineapp.model.Exercise
import com.gon.kineapp.model.Video
import kotlinx.android.synthetic.main.adapter_exercise.view.*

class ExerciseAdapter(private val exercises: MutableList<Exercise>, private val callback: ExerciseListener, private val isMedic: Boolean): RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder>() {

    interface ExerciseListener {
        fun onWatchVideo(video: Video)
        fun onExerciseMarkedAsDone(exercise: Exercise)
        fun onExerciseRemove(exercise: Exercise)
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ExerciseViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.adapter_exercise, p0, false)
        return ExerciseViewHolder(v)
    }

    override fun getItemCount(): Int {
        return exercises.size
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, pos: Int) {
        holder.bind(exercises[pos], callback, isMedic)
    }

    class ExerciseViewHolder(private var viewItem: View): RecyclerView.ViewHolder(viewItem) {

        fun bind(exercise: Exercise, callback: ExerciseListener, isMedic: Boolean) {
            viewItem.tvDescription.text = exercise.description
            if (exercise.done) {
                viewItem.ivDone.setImageResource(R.drawable.ic_done)
                viewItem.tvDone.text = viewItem.context.getString(R.string.exercice_done)
            } else {
                viewItem.ivDone.setImageResource(R.drawable.ic_undone)
                viewItem.tvDone.text = viewItem.context.getString(R.string.exercice_undone)
            }
            exercise.video?.let { video ->
                viewItem.llWatchVideo.visibility = View.VISIBLE
                viewItem.llWatchVideo.setOnClickListener { callback.onWatchVideo(video) }
            } ?: run {
                viewItem.llWatchVideo.visibility = View.GONE
            }
            if (isMedic) {
                viewItem.llDone.visibility = View.GONE
                viewItem.llRemove.visibility = View.VISIBLE
                viewItem.llRemove.setOnClickListener { callback.onExerciseRemove(exercise) }
            } else {
                viewItem.llRemove.visibility = View.GONE
                viewItem.llDone.visibility = View.VISIBLE
                viewItem.llDone.setOnClickListener { if (!exercise.done) callback.onExerciseMarkedAsDone(exercise) }
            }
        }
    }
}