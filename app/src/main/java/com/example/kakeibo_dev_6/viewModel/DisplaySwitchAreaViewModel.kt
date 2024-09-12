package com.example.kakeibo_dev_6.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.kakeibo_dev_6.component.utility.toDate
import com.example.kakeibo_dev_6.dao.CategoryDao
import com.example.kakeibo_dev_6.enum.DateProperty
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class DisplaySwitchAreaViewModel @Inject constructor(
    private val categoryDao: CategoryDao
) : ViewModel() {

    var dateProperty by mutableStateOf(DateProperty.WEEK.name)
    var selectCategory by mutableIntStateOf(0)
    var sort by mutableStateOf(false)
    var pageTransitionFlg by mutableStateOf(true)

    val category = categoryDao.loadAllCategories().distinctUntilChanged()


    var standardOfStartDate by mutableStateOf(Date())

    var customOfStartDate by mutableStateOf(defaultCustomOfStartDate())

    var customOfLastDate by mutableStateOf(Date())
    private fun defaultCustomOfStartDate(): Date {
        val date = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth())
        val dateStr = "${date} 12:00:00"
        return dateStr.toDate()?.let { dateStr.toDate() } ?: Date()
    }
}