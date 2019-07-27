package com.gon.kineapp.model.responses

import com.gon.kineapp.model.Video
import com.squareup.moshi.Json

data class PublicVideosListResponse(@Json(name = "data") val publicVideos: MutableList<Video>)