package com.example.kakeibo_dev_6.presentation.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kakeibo_dev_6.data.dao.CategoryDao
import com.example.kakeibo_dev_6.data.entity.Category
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingCategoryViewModel @Inject constructor(
    private val categoryDao: CategoryDao
) : ViewModel() {

    var categoryOrder by mutableIntStateOf(0)

    val categoryList = categoryDao.loadAllCategories().distinctUntilChanged()

    var standardOrderCategory: List<Category>? = null

    var replaceOrderCategory: List<Category>? = null

    // 更新
    fun updateCategory(category: Category) {
        viewModelScope.launch {
            category.categoryOrder = categoryOrder
            categoryDao.updateCategory(category)
        }
    }
}