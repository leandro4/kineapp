package com.gon.kineapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Session(var patientName: String,
              var date: String,
              var description: String,
              var images: MutableList<String>?): Parcelable