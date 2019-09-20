package com.gon.kineapp.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
class Exercise (var id: String,
                var name: String,
                var description: String,
                var done: Boolean,
                var video: Video?): Parcelable