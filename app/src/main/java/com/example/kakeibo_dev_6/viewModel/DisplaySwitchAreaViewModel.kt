package com.example.kakeibo_dev_6.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.kakeibo_dev_6.weekLastDate
import com.example.kakeibo_dev_6.weekStartDate

class DisplaySwitchAreaViewModel: ViewModel() {
    var startDate by mutableStateOf(weekStartDate())
    var lastDate by mutableStateOf(weekLastDate())
    var dateProperty by mutableStateOf("week")
    var sort by mutableStateOf(false)
}