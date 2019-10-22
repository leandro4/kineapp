package com.gon.kineapp.model.requests

import com.gon.kineapp.model.SharedMedic
import com.squareup.moshi.Json

class UpdateUserThumbnailBody(val base64Pic: ThumbnailBody) {
    data class ThumbnailBody(@Json(name = "picture_base64") val picture: String)
}