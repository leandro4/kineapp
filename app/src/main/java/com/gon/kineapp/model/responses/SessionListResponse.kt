package com.gon.kineapp.model.responses

import com.gon.kineapp.model.Session
import com.squareup.moshi.Json

data class SessionListResponse(@Json(name = "data") val patients: MutableList<Session>)