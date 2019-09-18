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

    companion object {
        fun getTag(name: String): PhotoTag? {
            return when (name) {
                "Frente" -> F
                "Espalda" -> E
                "Izquierda" -> I
                "Derecha" -> D
                else -> null
            }
        }
    }
}