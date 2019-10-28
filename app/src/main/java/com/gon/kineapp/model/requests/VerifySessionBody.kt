package com.gon.kineapp.model.requests

import com.squareup.moshi.Json

data class VerifySessionBody(@Json(name="secret_question_id") var questionId: Int, var answer: String)