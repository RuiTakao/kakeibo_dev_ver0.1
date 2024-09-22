package com.example.kakeibo_dev_6.presentation.expenditure_item.edit_expenditure_item

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.kakeibo_dev_6.domain.model.ExpenditureItem
import com.example.kakeibo_dev_6.domain.repository.CategoryDao
import com.example.kakeibo_dev_6.domain.repository.ExpenditureItemDao
import com.example.kakeibo_dev_6.presentation.ScreenRoute
import com.example.kakeibo_dev_6.common.utility.checkInt
import com.example.kakeibo_dev_6.common.utility.toDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class EditExpenditureItemViewModel @Inject constructor(
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

    val maxOrderCategory = categoryDao.maxOrderCategory().distinctUntilChanged()

    val category = categoryDao.loadAllCategories().distinctUntilChanged()

    fun setEditingExpendItem(id: Int): Flow<ExpenditureItem> {
        val oneOfExpendItem = expenditureItemDao.loadExpenditureItem(id = id).distinctUntilChanged()
        return oneOfExpendItem
    }

    var editingExpendItem: ExpenditureItem? = null

    fun updateExpendItem() {
        editingExpendItem?.let { expendItem ->
            viewModelScope.launch {
                expendItem.payDate = payDate
                expendItem.price = price
                expendItem.categoryId = categoryId
                expendItem.content = content
                expenditureItemDao.updateExpenditureItem(expendItem)
            }
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