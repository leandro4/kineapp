package com.gon.kineapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Video(
        var id: String,
        var title: String,
        var thumbUrl: String,
        var videoUrl: String,
        var public: Boolean): Parcelable