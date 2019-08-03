package com.gon.kineapp.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
open class User (open var username: String,
                 @Json(name="first_name") open var name: String,
                 @Json(name="last_name")open var surname: String,
                 open var phone: String,
                 @Json(name="email")open var mail: String,
                 open var number: String,
                 open var medic: Medic?,
                 open val type: String): Parcelable {

    fun isMedic(): Boolean {
        return medic != null
    }

    fun isPatient(): Boolean {
        return type == "patient"
    }
}