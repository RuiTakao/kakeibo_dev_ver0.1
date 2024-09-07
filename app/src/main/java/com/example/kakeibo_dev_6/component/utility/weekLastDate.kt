package com.example.kakeibo_dev_6.component.utility

import java.util.Calendar
import java.util.Date

fun weekLastDate(): Date {
    val calendar: Calendar = Calendar.getInstance()
    val lastDay = Calendar.getInstance()
    calendar.time = Date()
    lastDay.add(Calendar.DATE, 7 - calendar.get(Calendar.DAY_OF_WEEK))
    return lastDay.time
}