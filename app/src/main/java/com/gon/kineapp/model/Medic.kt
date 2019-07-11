package com.gon.kineapp.model

class Medic (
             var licence: String,
             id: String?,
             name: String,
             surname: String,
             phone: String,
             mail: String): User(id, name, surname, phone, mail) {

}