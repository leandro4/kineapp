package com.gon.kineapp.model.responses

import com.squareup.moshi.Json

data class SomethingResponse(@Json(name = "some") val  data: Boolean)