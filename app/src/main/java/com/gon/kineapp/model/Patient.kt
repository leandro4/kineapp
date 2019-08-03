package com.gon.kineapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Patient(
    var age: String,
    var gender: String,
    var videos: MutableList<Video>,
    override var username: String,
    override var name: String,
    override var surname: String,
    override var phone: String,
    override var mail: String,
    override var number: String,
    override var medic: Medic?,
    override var type: String): User(username, name, surname, phone, mail, number, medic, type), Parcelable