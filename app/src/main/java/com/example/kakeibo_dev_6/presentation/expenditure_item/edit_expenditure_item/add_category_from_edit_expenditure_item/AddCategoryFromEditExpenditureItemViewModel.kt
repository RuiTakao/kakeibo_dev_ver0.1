package com.example.kakeibo_dev_6.presentation.expenditure_item.edit_expenditure_item.add_category_from_edit_expenditure_item

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kakeibo_dev_6.domain.model.Category
import com.example.kakeibo_dev_6.domain.repository.CategoryDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddCategoryFromEditExpenditureItemViewModel @Inject constructor(
    private val categoryDao: CategoryDao
) : ViewModel() {
    var name by mutableStateOf("")
    var order by mutableIntStateOf(0)

    var inputValidateCategoryText by mutableStateOf("")

    val maxOrderCategory = categoryDao.maxOrderCategory().distinctUntilChanged()

    val categoryList = categoryDao.loadAllCategories().distinctUntilChanged()

    // 登録
    fun createCategory() {
        viewModelScope.launch {
            val newCategory = Category(categoryName = name, categoryOrder = order)
            categoryDao.insertCategory(newCategory)
        }
    }

    // バリデーション
    fun validate(value: String, categoryList: List<Category>?): Boolean {

        when {
            value == "" -> {
                inputValidateCategoryText = "カテゴリーが未入力です。"
                return true
            }

            value.length > 10 -> {
                inputValidateCategoryText = "カテゴリーは10文字以内で入力してください。"
                return true
            }

            else -> {
                // バリデーション、カテゴリー名重複
                try {
                    categoryList!!.forEach {
                        if (it.categoryName == value) {
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