package com.gon.kineapp.model.requests

import com.squareup.moshi.Json

data class PatientIdBody(@Json(name="patient_id") val patientId: String)