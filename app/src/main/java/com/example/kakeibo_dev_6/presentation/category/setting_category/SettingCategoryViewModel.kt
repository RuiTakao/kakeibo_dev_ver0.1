package com.example.kakeibo_dev_6.presentation.category.setting_category

import androidx.lifecycle.ViewModel
import com.example.kakeibo_dev_6.domain.model.Category
import com.example.kakeibo_dev_6.domain.repository.CategoryDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

@HiltViewModel
class SettingCategoryViewModel @Inject constructor(
    private val categoryDao: CategoryDao
): ViewModel() {
    val categoryList = categoryDao.loadAllCategories().distinctUntilChanged()

    var stateCategoryList: List<Category>? = null
}