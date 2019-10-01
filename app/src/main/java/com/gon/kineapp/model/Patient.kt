package com.gon.kineapp.model

import android.os.Parcelable
import android.util.ArrayMap
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
class Patient(
    @Json(name="current_medic_id") var medicLicense: String,
    @Json(name="exercises") var routine: Map<Int, MutableList<Exercise>>?): Parcelable