package com.example.kakeibo_dev_6.presentation.expenditure_item.expenditure_item_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kakeibo_dev_6.domain.model.ExpenditureItem
import com.example.kakeibo_dev_6.domain.repository.CategoryDao
import com.example.kakeibo_dev_6.domain.repository.ExpenditureItemDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExpenditureItemDetailViewModel @Inject constructor(
    private val expenditureItemDao: ExpenditureItemDao,
    private val categoryDao: CategoryDao
) : ViewModel() {

    val category = categoryDao.loadAllCategories().distinctUntilChanged()

    fun setEditingExpendItem(id: Int): Flow<ExpenditureItem> {
        return expenditureItemDao.loadExpenditureItem(id = id).distinctUntilChanged()
    }

    var editingExpendItem: ExpenditureItem? = null

    fun deleteExpendItem(expendItem: ExpenditureItem) {
        viewModelScope.launch {
            expenditureItemDao.deleteExpenditureItem(expendItem)
        }
    }
}