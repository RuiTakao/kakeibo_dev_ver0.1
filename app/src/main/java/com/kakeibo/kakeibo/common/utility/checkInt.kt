package com.kakeibo.kakeibo.common.utility

fun checkInt(str: String): Boolean {
    return try {
        str.toInt()
        true
    } catch (e: NumberFormatException) {
        false
    }
}