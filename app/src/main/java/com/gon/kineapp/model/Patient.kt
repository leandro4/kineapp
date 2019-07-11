package com.gon.kineapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Patient (
             val age: String,
             val gender: String,
             val id: String?,
             val name: String,
             var surname: String,
             val phone: String,
             var mail: String): Parcelable