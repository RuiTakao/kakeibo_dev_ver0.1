package com.example.kakeibo_dev_6.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kakeibo_dev_6.dao.CategoryDao
import com.example.kakeibo_dev_6.entity.Category
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReplaceOrderCategoryViewModel @Inject constructor(
    private val categoryDao: CategoryDao
) : ViewModel() {

    var categoryOrder by mutableStateOf(0)

    val category = categoryDao.loadAllCategories().distinctUntilChanged()

    var replaceOrderCategory: List<Category>? = null

    // 更新
    fun updateCategory(category: Category) {
        viewModelScope.launch {
            category.categoryOrder = categoryOrder
            categoryDao.updateCategory(category)
        }
    }
}