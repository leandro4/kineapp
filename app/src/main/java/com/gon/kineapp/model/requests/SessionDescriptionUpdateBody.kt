package com.gon.kineapp.model.requests

import com.squareup.moshi.Json

data class SessionDescriptionUpdateBody(@Json(name="description") val desc: String)