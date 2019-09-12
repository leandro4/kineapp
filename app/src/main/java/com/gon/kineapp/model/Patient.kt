package com.gon.kineapp.model

import android.os.Parcelable
import android.util.ArrayMap
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
class Patient(
    var videos: MutableList<Video>,
    @Json(name="current_medic_id") var medicLicense: String,
    @Json(name="current_medic_first_name") var medicFirstName: String? = "SinNombre",
    @Json(name="current_medic_last_name") var medicLastName: String? = "SinApellido",
    @Json(name="exercises") var routine: Map<Int, Routine>?): Parcelable