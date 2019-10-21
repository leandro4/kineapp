package com.gon.kineapp.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Video(
        var id: String,
        var name: String,
        @Json(name = "thumbnail_url") var thumbUrl: String? = null,
        var url: String): Parcelable