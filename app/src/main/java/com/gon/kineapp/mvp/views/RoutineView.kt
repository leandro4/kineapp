package com.gon.kineapp.mvp.views

import com.gon.kineapp.model.Exercise

interface RoutineView: BaseView {

    fun onExercisesEdited(ex: Exercise)
    fun onExercisesCreated(exercises: MutableList<Exercise>)
    fun onExerciseDeleted(id: String)
    fun onUserLoaded()

}