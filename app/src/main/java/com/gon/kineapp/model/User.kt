package com.gon.kineapp.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
open class User (open var id: String,
                 @Json(name="first_name") open var name: String,
                 @Json(name="last_name") open var surname: String,
                 @Json(name="email") open var mail: String,
                 @Json(name="dni") open var dni: String,
                 @Json(name="birth_date") open var birthdate: String,
                 open var medic: Medic?,
                 open var patient: Patient?): Parcelable {

    fun isMedic(): Boolean {
        return medic != null
    }

    fun isPatient(): Boolean {
        return medic == null
    }
}