package com.example.kakeibo_dev_6.component.utility

fun checkInt(str: String): Boolean {
    return try {
        str.toInt()
        true
    } catch (e: NumberFormatException) {
        false
    }
}