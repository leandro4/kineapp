package com.gon.kineapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Video(
        var id: String,
        var name: String,
        var thumbUrl: String? = null,
        var url: String): Parcelable