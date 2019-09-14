package com.gon.kineapp.model

enum class PhotoTag {

    F, D, I, E, O;

    fun getCompleteName(): String {
        return when(this) {
            F -> "Frente"
            E -> "Espalda"
            I -> "Izquierda"
            D -> "Derecha"
            else -> "Otra"
        }
    }
}