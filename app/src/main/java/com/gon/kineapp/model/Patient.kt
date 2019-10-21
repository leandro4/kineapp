package com.gon.kineapp.model

import android.os.Parcelable
import android.util.ArrayMap
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
class Patient(
    @Json(name="current_medic") var currentMedic: SharedMedic,
    @Json(name="shared_history_with") var readOnlyMedics: MutableList<SharedMedic>,
    @Json(name="exercises") var routine: Map<Int, MutableList<Exercise>>?): Parcelable