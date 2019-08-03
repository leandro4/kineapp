package com.gon.kineapp.model.requests

import com.squareup.moshi.Json

class RegisterUserBody(@Json(name="google_token") var googleToken: String)