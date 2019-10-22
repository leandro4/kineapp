package com.gon.kineapp.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Session(
    var id: String,
    var date: String,
    var description: String,
    var readOnly: Boolean = false,
    @Json(name="images") var photos: MutableList<Photo>): Parcelable