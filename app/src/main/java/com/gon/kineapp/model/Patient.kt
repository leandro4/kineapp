package com.gon.kineapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Patient(
    var age: String,
    var gender: String,
    override var id: String,
    override var name: String,
    override var surname: String,
    override var phone: String,
    override var mail: String,
    override var number: String): User(id, name, surname, phone, mail, number), Parcelable