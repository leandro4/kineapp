package com.gon.kineapp.utils

import kotlin.Comparable

fun <T: Comparable<in T>> equalTo(some: T): (T) -> Boolean {
    return fun(other: T): Boolean {
        return some == other
    }
}