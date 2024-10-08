package com.kakeibo.kakeibo_dev_6.presentation.category.setting_category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kakeibo.kakeibo_dev_6.domain.model.Category
import com.kakeibo.kakeibo_dev_6.domain.repository.CategoryDao
import com.kakeibo.kakeibo_dev_6.domain.repository.ExpenditureItemDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingCategoryViewModel @Inject constructor(
    private val categoryDao: CategoryDao,
    expenditureItemDao: ExpenditureItemDao
): ViewModel() {

    // カテゴリー一覧取得
    val categoryList = categoryDao.loadAllCategories().distinctUntilChanged()

    // 取得したカテゴリーをViewModelに保存
    var category: Category? = null

    // 取得したカテゴリーリストをViewModelに保存
    var stateCategoryList: List<Category>? = null

    // 支出項目リスト、カテゴリーが使用されているか確認する為
    val expenditureItemList = expenditureItemDao.loadAllExpenditureItems().distinctUntilChanged()

    /**
     * カテゴリー削除
     *
     * @return Unit
     */
    fun deleteCategory(category: Category) {
        viewModelScope.launch {
            categoryDao.deleteCategory(category)
        }
    }
}