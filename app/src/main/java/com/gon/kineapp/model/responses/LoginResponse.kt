package com.gon.kineapp.model.responses

import com.gon.kineapp.model.User
import com.squareup.moshi.Json

data class LoginResponse(@Json(name = "user") val  user: User)