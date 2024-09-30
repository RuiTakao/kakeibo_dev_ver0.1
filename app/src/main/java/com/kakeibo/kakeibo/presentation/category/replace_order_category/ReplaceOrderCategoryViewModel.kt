package com.kakeibo.kakeibo.presentation.category.replace_order_category

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kakeibo.kakeibo.domain.model.Category
import com.kakeibo.kakeibo.domain.repository.CategoryDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReplaceOrderCategoryViewModel @Inject constructor(
    private val categoryDao: CategoryDao
) : ViewModel() {

    // カテゴリーの並び順番号
    var categoryOrder by mutableIntStateOf(0)

    // カテゴリー設定で取得したカテゴリーを保存
    var stateCategoryList: List<Category>? = null

    /**
     * 更新
     *
     * @param category Category
     *
     * @return Unit
     */
    fun updateCategory(category: Category) {
        viewModelScope.launch {
            category.categoryOrder = categoryOrder
            categoryDao.updateCategory(category)
        }
    }
}