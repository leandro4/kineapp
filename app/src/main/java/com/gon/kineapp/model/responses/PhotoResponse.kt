package com.gon.kineapp.model.responses

data class PhotoResponse(
    var id: String,
    var thumbnail: String,
    var content: String? = null,
    var tag: String)