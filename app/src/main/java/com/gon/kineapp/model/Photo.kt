package com.gon.kineapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Photo(
        var id: String,
        var thumbUrl: String,
        var imgUrl: String,
        var tag: String): Parcelable