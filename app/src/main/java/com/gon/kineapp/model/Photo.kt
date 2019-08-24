package com.gon.kineapp.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Photo(
        var id: String,
        var thumbnail: String,
        var tag: String): Parcelable