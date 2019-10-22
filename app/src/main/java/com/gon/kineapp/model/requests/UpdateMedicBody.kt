package com.gon.kineapp.model.requests

import com.gon.kineapp.model.SharedMedic
import com.squareup.moshi.Json

class UpdateMedicBody(val patient: SharedMedicBody) {
    data class SharedMedicBody(@Json(name = "current_medic") val sharedMedic: SharedMedic)
}