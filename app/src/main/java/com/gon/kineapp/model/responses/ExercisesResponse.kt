package com.gon.kineapp.model.responses

import com.gon.kineapp.model.Exercise
import com.squareup.moshi.Json

data class ExercisesResponse(@Json(name="data") val exercises: MutableList<Exercise>)