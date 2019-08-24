package com.gon.kineapp.mvp.views

import com.gon.kineapp.model.ExercisesCalendar

interface ExercisesView: BaseView {

    fun onExercisesResponse(exercises: MutableList<ExercisesCalendar>)
    fun onExercisesEdited()

}