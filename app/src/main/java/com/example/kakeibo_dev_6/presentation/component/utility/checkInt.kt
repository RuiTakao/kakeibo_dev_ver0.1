package com.example.kakeibo_dev_6.presentation.component.utility

fun checkInt(str: String): Boolean {
    return try {
        str.toInt()
        true
    } catch (e: NumberFormatException) {
        false
    }
}