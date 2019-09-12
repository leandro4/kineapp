package com.gon.kineapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Routine (val exercises: MutableList<Exercise>): Parcelable