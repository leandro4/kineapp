package com.gon.kineapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Video(
        var id: String,
        var name: String,
        var thumbUrl: String? = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRAXat2LsnZ6uQbLHtIq0PLEzedF1fqftTSgrJtGidGEKl0YqM-",
        var url: String): Parcelable