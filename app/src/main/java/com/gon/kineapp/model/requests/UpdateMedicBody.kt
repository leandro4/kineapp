package com.gon.kineapp.model.requests

import com.squareup.moshi.Json

class UpdateMedicBody(val patient: License) {
    data class License(@Json(name = "current_medic_id") val license: String)
}