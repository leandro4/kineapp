package com.gon.kineapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Patient(
    var videos: MutableList<Video>): Parcelable