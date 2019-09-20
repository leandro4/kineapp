package com.gon.kineapp.model.requests

import com.squareup.moshi.Json

data class NewExerciseBody (@Json(name="patient_id") val patientId: String, val name: String, val description: String, @Json(name="video_id") val videoId: String?, val days: List<Int>)