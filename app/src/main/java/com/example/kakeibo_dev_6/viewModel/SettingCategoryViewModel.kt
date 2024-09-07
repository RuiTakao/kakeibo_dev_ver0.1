package com.example.kakeibo_dev_6.viewModel

import androidx.lifecycle.ViewModel
import com.example.kakeibo_dev_6.dao.CategoryDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

@HiltViewModel
class SettingCategoryViewModel @Inject constructor(
    private val categoryDao: CategoryDao
) : ViewModel() {
    val category = categoryDao.loadAllCategories().distinctUntilChanged()
}