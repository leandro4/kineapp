package com.gon.kineapp.model

import com.gon.kineapp.R

enum class PhotoTag {

    F {
        override fun getDrawable(width: Int, height: Int): Int {
            return when (width) {
                1 -> R.drawable.front_back_large_2
                2 -> R.drawable.front_back_large_3
                3 -> R.drawable.front_back_large_4
                else -> R.drawable.front_back_large_1
            }
        }
    },
    D {
        override fun getDrawable(width: Int, height: Int): Int {
            return when (width) {
                1 -> R.drawable.front_back_large_2
                2 -> R.drawable.front_back_large_3
                3 -> R.drawable.front_back_large_4
                else -> R.drawable.front_back_large_1
            }
        }
    },
    I {
        override fun getDrawable(width: Int, height: Int): Int {
            return when (width) {
                1 -> R.drawable.front_back_large_2
                2 -> R.drawable.front_back_large_3
                3 -> R.drawable.front_back_large_4
                else -> R.drawable.front_back_large_1
            }
        }
    },
    E {
        override fun getDrawable(width: Int, height: Int): Int {
            return when (width) {
                1 -> R.drawable.front_back_large_2
                2 -> R.drawable.front_back_large_3
                3 -> R.drawable.front_back_large_4
                else -> R.drawable.front_back_large_1
            }
        }
    },
    O {
        override fun getDrawable(width: Int, height: Int): Int {
            return when (width) {
                1 -> R.drawable.front_back_large_2
                2 -> R.drawable.front_back_large_3
                3 -> R.drawable.front_back_large_4
                else -> R.drawable.front_back_large_1
            }
        }
    };

    fun getCompleteName(): String {
        return when(this) {
            F -> "Frente"
            E -> "Espalda"
            I -> "Izquierda"
            D -> "Derecha"
            else -> "Otra"
        }
    }

    abstract fun getDrawable(width: Int, height: Int): Int

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