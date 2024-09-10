package com.example.kakeibo_dev_6.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kakeibo_dev_6.dao.CategoryDao
import com.example.kakeibo_dev_6.dao.ExpenditureItemDao
import com.example.kakeibo_dev_6.entity.ExpenditureItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditExpenditureItemViewModel @Inject constructor(
    private val expendItemDao: ExpenditureItemDao,
    private val categoryDao: CategoryDao
) : ViewModel() {
    var payDate by mutableStateOf("")
    var category_id by mutableStateOf("")
    var content by mutableStateOf("")
    var price by mutableStateOf("")

    var viewPayDate by mutableStateOf("")

    var createCategoryFlg by mutableStateOf(false)
    var firstCategory by mutableStateOf(0)

    val maxOrderCategory = categoryDao.maxOrderCategory().distinctUntilChanged()

    val category = categoryDao.loadAllCategories().distinctUntilChanged()

    fun createExpendItem() {
        viewModelScope.launch {
            val newExpendItem = ExpenditureItem(
                payDate = payDate,
                categoryId = category_id,
                content = content,
                price = price
            )
            expendItemDao.insertExpenditureItem(newExpendItem)
        }
    }

    fun setEditingExpendItem(id: Int): Flow<ExpenditureItem> {
        val oneOfExpendItem = expendItemDao.loadExpenditureItem(id = id).distinctUntilChanged()
        return oneOfExpendItem
    }

    var editingExpendItem: ExpenditureItem? = null

    fun updateExpendItem() {
        editingExpendItem?.let { expendItem ->
            viewModelScope.launch {
                expendItem.payDate = payDate
                expendItem.price = price
                expendItem.categoryId = category_id
                expendItem.content = content
                expendItemDao.updateExpenditureItem(expendItem)
            }
        }
    }

    fun deleteExpendItem(expendItem: ExpenditureItem) {
        viewModelScope.launch {
            expendItemDao.deleteExpenditureItem(expendItem)
        }
    }

    var inputValidatePayDateStatus by mutableStateOf(false)
    var inputValidatePayDateText by mutableStateOf("")
    var inputValidatePriceStatus by mutableStateOf(false)
    var inputValidatePriceText by mutableStateOf("")
    var inputValidateSelectCategoryStatus by mutableStateOf(false)
    var inputValidateSelectCategoryText by mutableStateOf("")
    var inputValidateContentStatus by mutableStateOf(false)
    var inputValidateContentText by mutableStateOf("")
}