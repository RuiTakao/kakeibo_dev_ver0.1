package com.example.kakeibo_dev_6.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kakeibo_dev_6.dao.CategoryDao
import com.example.kakeibo_dev_6.dao.ExpenditureItemDao
import com.example.kakeibo_dev_6.entity.Category
import com.example.kakeibo_dev_6.entity.ExpenditureItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditCategoryViewModel @Inject constructor(
    private val categoryDao: CategoryDao,
    private val expenditureItemDao: ExpenditureItemDao
) : ViewModel() {
    var editingCategory: Category? = null

    var name by mutableStateOf("")
    var order by mutableIntStateOf(0)

    var inputValidateCategoryStatus by mutableStateOf(false)
    var inputValidateCategoryText by mutableStateOf("")

    // 登録
    fun createCategory() {
        viewModelScope.launch {
            val newCategory = Category(categoryName = name, categoryOrder = order)
            categoryDao.insertCategory(newCategory)
        }
    }

    fun setEditingCategory(id: Int): Flow<Category> {
        val oneOfCategory = categoryDao.getOneOfCategory(id = id).distinctUntilChanged()
        return oneOfCategory
    }

    // 更新
    fun updateCategory() {
        editingCategory?.let { category ->
            viewModelScope.launch {
                category.categoryName = name
                categoryDao.updateCategory(category)
            }
        }
    }

    // 削除
    fun deleteCategory(category: Category) {
        viewModelScope.launch {
            categoryDao.deleteCategory(category)
        }
    }

    val maxOrderCategory = categoryDao.maxOrderCategory().distinctUntilChanged()

    fun isUsedCategory(categoryId: Int): Flow<List<ExpenditureItem>> {
        return expenditureItemDao.isUsedCategory(categoryId = categoryId)
    }
}