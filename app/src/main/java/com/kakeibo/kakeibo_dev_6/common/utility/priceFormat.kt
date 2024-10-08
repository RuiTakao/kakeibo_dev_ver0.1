package com.kakeibo.kakeibo_dev_6.common.utility

fun priceFormat(priceText: String): String {
    return when {
        checkInt(priceText) -> String.format("%,d", priceText.toInt())
        else -> ""
    }
}