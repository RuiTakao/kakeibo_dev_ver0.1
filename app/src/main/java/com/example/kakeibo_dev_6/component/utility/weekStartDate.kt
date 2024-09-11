package com.example.kakeibo_dev_6.component.utility

import java.util.Calendar
import java.util.Date

fun weekStartDate(date: Date = Date()): Date {
    val calendar: Calendar = Calendar.getInstance()
    val firstDay = Calendar.getInstance()
    calendar.time = date
    firstDay.time = date
    firstDay.add(Calendar.DATE, (calendar.get(Calendar.DAY_OF_WEEK) - 1) * -1)
    return firstDay.time
}