package com.gon.kineapp.model.requests

import com.squareup.moshi.Json

class UserExistsBody( @Json(name="google_token") val googleToken: String)