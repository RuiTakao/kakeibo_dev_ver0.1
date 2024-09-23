package com.example.kakeibo_dev_6.presentation.category.edit_category

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kakeibo_dev_6.domain.model.Category
import com.example.kakeibo_dev_6.domain.model.ExpenditureItem
import com.example.kakeibo_dev_6.domain.repository.CategoryDao
import com.example.kakeibo_dev_6.domain.repository.ExpenditureItemDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditCategoryViewModel @Inject constructor(
    private val categoryDao: CategoryDao,
    private val expenditureItemDao: ExpenditureItemDao,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    var editingCategory: Category? = null

    var name by mutableStateOf("")
    var order by mutableIntStateOf(0)

    var inputValidateCategoryText by mutableStateOf("")

    val maxOrderCategory = categoryDao.maxOrderCategory().distinctUntilChanged()

    val categoryList = categoryDao.loadAllCategories().distinctUntilChanged()

    fun setEditingCategory(id: Int): Flow<Category> {
        return categoryDao.getOneOfCategory(id = id).distinctUntilChanged()
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

    // 登録
    fun createCategory() {
        viewModelScope.launch {
            val newCategory = Category(categoryName = name, categoryOrder = order)
            categoryDao.insertCategory(newCategory)
        }
    }

    // 削除
    fun deleteCategory(category: Category) {
        viewModelScope.launch {
            categoryDao.deleteCategory(category)
        }
    }

    fun isUsedCategory(categoryId: Int): Flow<List<ExpenditureItem>> {
        return expenditureItemDao.isUsedCategory(categoryId = categoryId)
    }

    // バリデーション
    fun validate(value: String, id: Int, categoryList: List<Category>?): Boolean {

        when {
            value == "" -> {
                inputValidateCategoryText = "カテゴリーが未入力です。"
                println("text none")
                return true
            }

            value.length > 10 -> {
                inputValidateCategoryText = "カテゴリーは10文字以内で入力してください。"
                println("text > 10")
                return true
            }

            else -> {
                // バリデーション、カテゴリー名重複
                try {
                    categoryList!!.forEach {
                        if (it.categoryName == value) {
                            if (id == it.id) {
                                return@forEach
                            }
                            inputValidateCategoryText = "カテゴリー名が重複しています。"
                            return true
                        }
                    }
                } catch (e: NullPointerException) {
                    inputValidateCategoryText = "重大なエラーが発生しました、開発者に問い合わせしてください。"
                    return true
                }

                inputValidateCategoryText = ""
                return false
            }
        }
    }
}