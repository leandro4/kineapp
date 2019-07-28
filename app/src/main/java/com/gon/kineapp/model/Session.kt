package com.gon.kineapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Session(
              var id: String,
              var patientId: String,
              var date: String,
              var description: String,
              var photos: MutableList<Photo>): Parcelable