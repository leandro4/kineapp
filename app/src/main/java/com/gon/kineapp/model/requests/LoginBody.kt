package com.gon.kineapp.model.requests

import com.squareup.moshi.Json

class LoginBody(@Json(name="google_token") var googleToken: String, @Json(name="secret_question_id") var questionId: Int, var answer: String)