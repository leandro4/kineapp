package com.gon.kineapp.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SharedMedic (val id: String, @Json(name = "first_name") val name: String, @Json(name = "last_name") val surname: String): Parcelable