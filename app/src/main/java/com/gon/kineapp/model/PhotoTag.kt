package com.gon.kineapp.model

import com.gon.kineapp.R

enum class PhotoTag {

    F {
        override fun getDrawable(width: Int, height: Int): Int {
            return when (width) {
                1 -> {
                    when (height) {
                        1 -> R.drawable.front_back_medium_2
                        2 -> R.drawable.front_back_large_2
                        else -> R.drawable.front_back_small_2
                    }
                }
                2 -> {
                    when (height) {
                        1 -> R.drawable.front_back_medium_3
                        2 -> R.drawable.front_back_large_3
                        else -> R.drawable.front_back_small_3
                    }
                }
                3 -> {
                    when (height) {
                        1 -> R.drawable.front_back_medium_4
                        2 -> R.drawable.front_back_large_4
                        else -> R.drawable.front_back_small_4
                    }
                }
                else -> {
                    when (height) {
                        1 -> R.drawable.front_back_medium_1
                        2 -> R.drawable.front_back_large_1
                        else -> R.drawable.front_back_small_1
                    }
                }
            }
        }
    },
    D {
        override fun getDrawable(width: Int, height: Int): Int {
            return when (width) {
                1 -> {
                    when (height) {
                        1 -> R.drawable.rigth_medium_2
                        2 -> R.drawable.rigth_large_2
                        else -> R.drawable.rigth_small_2
                    }
                }
                2 -> {
                    when (height) {
                        1 -> R.drawable.rigth_medium_3
                        2 -> R.drawable.rigth_large_3
                        else -> R.drawable.rigth_small_3
                    }
                }
                3 -> {
                    when (height) {
                        1 -> R.drawable.rigth_medium_4
                        2 -> R.drawable.rigth_large_4
                        else -> R.drawable.rigth_small_4
                    }
                }
                else -> {
                    when (height) {
                        1 -> R.drawable.rigth_medium_1
                        2 -> R.drawable.rigth_large_1
                        else -> R.drawable.rigth_small_1
                    }
                }
            }
        }
    },
    I {
        override fun getDrawable(width: Int, height: Int): Int {
            return when (width) {
                1 -> {
                    when (height) {
                        1 -> R.drawable.left_medium_2
                        2 -> R.drawable.left_large_2
                        else -> R.drawable.left_small_2
                    }
                }
                2 -> {
                    when (height) {
                        1 -> R.drawable.left_medium_3
                        2 -> R.drawable.left_large_3
                        else -> R.drawable.left_small_3
                    }
                }
                3 -> {
                    when (height) {
                        1 -> R.drawable.left_medium_4
                        2 -> R.drawable.left_large_4
                        else -> R.drawable.left_small_4
                    }
                }
                else -> {
                    when (height) {
                        1 -> R.drawable.left_medium_1
                        2 -> R.drawable.left_large_1
                        else -> R.drawable.left_small_1
                    }
                }
            }
        }
    },
    E {
        override fun getDrawable(width: Int, height: Int): Int {
            return when (width) {
                1 -> {
                    when (height) {
                        1 -> R.drawable.front_back_medium_2
                        2 -> R.drawable.front_back_large_2
                        else -> R.drawable.front_back_small_2
                    }
                }
                2 -> {
                    when (height) {
                        1 -> R.drawable.front_back_medium_3
                        2 -> R.drawable.front_back_large_3
                        else -> R.drawable.front_back_small_3
                    }
                }
                3 -> {
                    when (height) {
                        1 -> R.drawable.front_back_medium_4
                        2 -> R.drawable.front_back_large_4
                        else -> R.drawable.front_back_small_4
                    }
                }
                else -> {
                    when (height) {
                        1 -> R.drawable.front_back_medium_1
                        2 -> R.drawable.front_back_large_1
                        else -> R.drawable.front_back_small_1
                    }
                }
            }
        }
    },
    O {
        override fun getDrawable(width: Int, height: Int): Int {
            return 0
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