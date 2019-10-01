package com.gon.kineapp.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
class Medic(
    var videos: MutableList<Video>,
    var license: String) : Parcelable