package com.gon.kineapp.model.responses

import com.gon.kineapp.model.Photo
import com.gon.kineapp.model.Video
import com.squareup.moshi.Json

data class PhotosListResponse(@Json(name = "data") val photos: MutableList<Photo>)