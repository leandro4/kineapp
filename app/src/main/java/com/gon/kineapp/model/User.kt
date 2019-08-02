package com.gon.kineapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
open class User (open var id: String,
                 open var name: String,
                 open var surname: String,
                 open var phone: String,
                 open var mail: String,
                 open var number: String,
                 open val type: String): Parcelable {

    fun isMedic(): Boolean {
        return type == "medic"
    }

    fun isPatient(): Boolean {
        return type == "patient"
    }
}