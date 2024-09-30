package com.kakeibo.kakeibo.common

import java.text.SimpleDateFormat
import java.util.Locale

object Constants {
    val DATE_INSERT_FORMAT = SimpleDateFormat("yyyy-MM-dd", Locale.JAPANESE)
    val DATE_VIEW_FORMAT = SimpleDateFormat("M月d日", Locale.JAPANESE)
    val MONTH_VIEW_FORMAT = SimpleDateFormat("M月", Locale.JAPANESE)
}