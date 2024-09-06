package com.example.kakeibo_dev_6.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.kakeibo_dev_6.component.utility.weekLastDate
import com.example.kakeibo_dev_6.component.utility.weekStartDate
import com.example.kakeibo_dev_6.dao.CategoryDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

@HiltViewModel
class DisplaySwitchAreaViewModel @Inject constructor(
    private val categoryDao: CategoryDao
) : ViewModel() {
    var startDate by mutableStateOf(weekStartDate())
    var lastDate by mutableStateOf(weekLastDate())
    var dateProperty by mutableStateOf("week")
    var selectCategory by mutableStateOf(0)
    var sort by mutableStateOf(false)
    var pageTransitionFlg by mutableStateOf(true)

    val category = categoryDao.loadAllCategories().distinctUntilChanged()
}