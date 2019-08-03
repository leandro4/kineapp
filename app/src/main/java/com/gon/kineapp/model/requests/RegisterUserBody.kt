package com.gon.kineapp.model.requests

import com.squareup.moshi.Json

data class RegisterUserBody(@Json(name="google_token") var googleToken: String, @Json(name="first_name") var firstName: String,
                            @Json(name="last_name") var lastName: String, var license: String?, var email: String,
                            @Json(name="secret_question_id") var questionId: Int, var answer: String)