package com.gon.kineapp.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
class ExercisesCalendar (@Json(name="day") var day: Int,
                         @Json(name="description") var description: String,
                         @Json(name="video_url") var videoUrl: String?): Parcelable