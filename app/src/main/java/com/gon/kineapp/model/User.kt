package com.gon.kineapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
open class User (var id: String?,
            var name: String,
            var surname: String,
            var phone: String,
            var mail: String): Parcelable