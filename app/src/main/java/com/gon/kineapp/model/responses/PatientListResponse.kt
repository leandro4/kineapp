package com.gon.kineapp.model.responses

import com.gon.kineapp.model.User
import com.squareup.moshi.Json

data class PatientListResponse(@Json(name = "non_assigned_patients") val readOnlyPatients: MutableList<User>, val patients: MutableList<User>)