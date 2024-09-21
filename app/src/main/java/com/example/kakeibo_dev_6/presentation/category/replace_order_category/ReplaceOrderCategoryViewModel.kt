package com.example.kakeibo_dev_6.presentation.category.replace_order_category

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kakeibo_dev_6.domain.model.Category
import com.example.kakeibo_dev_6.domain.repository.CategoryDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReplaceOrderCategoryViewModel @Inject constructor(
    private val categoryDao: CategoryDao
) : ViewModel() {

    var categoryOrder by mutableIntStateOf(0)

    var stateCategoryList: List<Category>? = null

    // 更新
    fun updateCategory(category: Category) {
        viewModelScope.launch {
            category.categoryOrder = categoryOrder
            categoryDao.updateCategory(category)
        }
    }
}