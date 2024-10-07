package com.kakeibo.kakeibo_dev_6.common.utility

fun checkInt(str: String): Boolean {
    return try {
        str.toInt()
        true
    } catch (e: NumberFormatException) {
        false
    }
}