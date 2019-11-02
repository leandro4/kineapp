package com.gon.kineapp.model.requests

import com.squareup.moshi.Json

data class UpdateUserThumbnailBody(@Json(name = "picture_base64") val base64Pic: String)