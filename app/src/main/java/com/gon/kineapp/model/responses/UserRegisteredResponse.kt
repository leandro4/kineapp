package com.gon.kineapp.model.responses

import com.gon.kineapp.model.Question
import com.gon.kineapp.model.User
import com.squareup.moshi.Json

data class UserRegisteredResponse(@Json(name = "user") val myUser: User, val questions: List<Question>)