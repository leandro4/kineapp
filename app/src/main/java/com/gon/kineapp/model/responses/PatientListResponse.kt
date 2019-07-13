package com.gon.kineapp.model.responses

import com.gon.kineapp.model.Patient
import com.squareup.moshi.Json

data class PatientListResponse(@Json(name = "data") val patients: MutableList<Patient>)