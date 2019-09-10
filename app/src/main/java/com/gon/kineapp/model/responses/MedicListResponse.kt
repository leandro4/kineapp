package com.gon.kineapp.model.responses

import com.gon.kineapp.model.User
import com.squareup.moshi.Json

data class MedicListResponse (@Json(name="data") val medics: List<User>)