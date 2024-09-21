package com.example.kakeibo_dev_6.presentation.expenditure_item.add_expenditure_item

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kakeibo_dev_6.domain.model.ExpenditureItem
import com.example.kakeibo_dev_6.domain.repository.CategoryDao
import com.example.kakeibo_dev_6.domain.repository.ExpenditureItemDao
import com.example.kakeibo_dev_6.presentation.component.utility.checkInt
import com.example.kakeibo_dev_6.presentation.component.utility.toDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddExpenditureItemViewModel @Inject constructor(
    private val expenditureItemDao: ExpenditureItemDao,
    categoryDao: CategoryDao
) : ViewModel() {
    var payDate by mutableStateOf("")
    var categoryId by mutableStateOf("")
    var content by mutableStateOf("")
    var price by mutableStateOf("")

    var viewPayDate by mutableStateOf("")

    var createCategoryFlg by mutableStateOf(false)
    var firstCategory by mutableIntStateOf(0)

    val category = categoryDao.loadAllCategories().distinctUntilChanged()

    fun createExpendItem() {
        viewModelScope.launch {
            val newExpendItem = ExpenditureItem(
                payDate = payDate,
                categoryId = categoryId,
                content = content,
                price = price
            )
            expenditureItemDao.insertExpenditureItem(newExpendItem)
        }
    }

    var inputValidatePayDateText by mutableStateOf("")
    var inputValidatePriceText by mutableStateOf("")
    var inputValidateSelectCategoryText by mutableStateOf("")
    var inputValidateContentText by mutableStateOf("")

    fun validate(payDate: String, price: String, categoryId: String, content: String): Boolean {
        var validCount = 0

        inputValidatePayDateText =
            if (payDate.toDate("yyyy-MM-dd") == null) {
                validCount++
                "日付を入力してください。"
            } else ""

        inputValidatePriceText =
            if (price == "") {
                validCount++
                "金額を入力してください。"
            } else if (!checkInt(price)) {
                validCount++
                "金額が不正です。"
            } else ""

        inputValidateSelectCategoryText =
            if (categoryId == "") {
                validCount++
                "カテゴリーが未選択です。"
            } else ""

        inputValidateContentText =
            if (content == "") {
                validCount++
                "内容が未入力です。"
            } else if (content.length > 50) {
                validCount++
                "内容は50文字以内で入力してください。"
            } else ""

        return validCount == 0
    }
}