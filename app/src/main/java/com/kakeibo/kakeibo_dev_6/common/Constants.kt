package com.kakeibo.kakeibo_dev_6.common

import java.text.SimpleDateFormat
import java.util.Locale

object Constants {
    val DATE_INSERT_FORMAT = SimpleDateFormat("yyyy-MM-dd", Locale.JAPANESE)
    val DATE_VIEW_FORMAT = SimpleDateFormat("M月d日", Locale.JAPANESE)
    val MONTH_VIEW_FORMAT = SimpleDateFormat("M月", Locale.JAPANESE)
    const val NONE_REGISTERED_USER_ALLOW_REFERENCE_DATE = 2
    // 課金システムが未完成の為、現在のテスターユーザーには課金している前提で使用してもらう
    const val IS_REGISTERED_USER = true
}