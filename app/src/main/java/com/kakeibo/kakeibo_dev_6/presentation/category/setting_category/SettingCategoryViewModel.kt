package com.kakeibo.kakeibo_dev_6.presentation.category.setting_category

import androidx.lifecycle.ViewModel
import com.kakeibo.kakeibo_dev_6.domain.model.Category
import com.kakeibo.kakeibo_dev_6.domain.repository.CategoryDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

@HiltViewModel
class SettingCategoryViewModel @Inject constructor(
    categoryDao: CategoryDao
): ViewModel() {

    // カテゴリー一覧取得
    val categoryList = categoryDao.loadAllCategories().distinctUntilChanged()

    // 取得したカテゴリーをViewModelに保存
    var stateCategoryList: List<Category>? = null
}