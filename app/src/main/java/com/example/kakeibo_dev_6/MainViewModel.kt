package com.example.kakeibo_dev_6

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kakeibo_dev_6.dao.CategorizeExpenditureItemDao
import com.example.kakeibo_dev_6.dao.CategoryDao
import com.example.kakeibo_dev_6.dao.ExpenditureItemDao
import com.example.kakeibo_dev_6.dao.ExpenditureItemJoinCategoryDao
import com.example.kakeibo_dev_6.entity.CategorizeExpenditureItem
import com.example.kakeibo_dev_6.entity.Category
import com.example.kakeibo_dev_6.entity.ExpenditureItem
import com.example.kakeibo_dev_6.entity.ExpenditureItemJoinCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val expendItemDao: ExpenditureItemDao,
    private val categoryDao: CategoryDao,
    private val categorizeExpenditureItemDao: CategorizeExpenditureItemDao,
    private val expenditureItemJoinCategoryDao: ExpenditureItemJoinCategoryDao
) : ViewModel() {
    var payDate by mutableStateOf("")
    var category_id by mutableStateOf("")
    var content by mutableStateOf("")
    var price by mutableStateOf("")
    var name by mutableStateOf("")
    var order by mutableStateOf(0)

    val expendItem = expendItemDao.loadAllExpenditureItems().distinctUntilChanged()
    val category = categoryDao.loadAllCategories().distinctUntilChanged()

    fun expenditureItemList(
        firstDay: String,
        lastDay: String,
        categoryId: Int,
        sort: Boolean
    ): Flow<List<ExpenditureItemJoinCategory>> {
        return if (sort) {
            expenditureItemJoinCategoryDao.loadAllExpenditureItemOrderAsc(
                firstDay = firstDay,
                lastDay = lastDay,
                category = categoryId
            )
                .distinctUntilChanged()
        } else {
            expenditureItemJoinCategoryDao.loadAllExpenditureItemOrderDesc(
                firstDay = firstDay,
                lastDay = lastDay,
                category = categoryId
            )
                .distinctUntilChanged()
        }
    }

    fun categorizeExpenditureItem(
        firstDay: String,
        lastDay: String
    ): Flow<List<CategorizeExpenditureItem>> {
        return categorizeExpenditureItemDao.categorizeExpenditureItem(
            firstDay = firstDay,
            lastDay = lastDay
        ).distinctUntilChanged()
    }

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

    fun setEditingExpendItem(id: Int): Flow<ExpenditureItem> {
        val oneOfExpendItem = expendItemDao.loadExpenditureItem(id = id).distinctUntilChanged()
        return oneOfExpendItem
    }

//    fun OneOfGroupCategory(id: Int): Flow<ExpenditureItemWithCategory> {
//        return groupCategoryDao.OneOfGroupCategory(id = id).distinctUntilChanged()
//    }

    var setGroupCategory: CategorizeExpenditureItem? = null

    var editingCategory: Category? = null

    var editingExpendItem: ExpenditureItem? = null

    fun updateCategory() {
        editingCategory?.let { category ->
            viewModelScope.launch {
                category.categoryName = name
                categoryDao.updateCategory(category)
            }
        }
    }

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

    fun deleteCategory(category: Category) {
        viewModelScope.launch {
            categoryDao.deleteCategory(category)
        }
    }

    fun deleteExpendItem(expendItem: ExpenditureItem) {
        viewModelScope.launch {
            expendItemDao.deleteExpenditureItem(expendItem)
        }
    }


    var startDate by mutableStateOf(weekStartDate())
    var lastDate by mutableStateOf(weekLastDate())
    var dateProperty by mutableStateOf("week")


    var payDetailStartDate by mutableStateOf(weekStartDate())
    var payDetailLastDate by mutableStateOf(weekLastDate())
    var payDetailDateProperty by mutableStateOf("week")
    var sort by mutableStateOf(false)
    var selectCategory by mutableStateOf(0)
    var selectCategoryName by mutableStateOf("すべて")
    var pageTransitionFlg by mutableStateOf(true)

    var inputValidateCategoryStatus by mutableStateOf(false)
    var inputValidateCategoryText by mutableStateOf("")
}

fun weekStartDate(): Date {
    val calendar: Calendar = Calendar.getInstance()
    val firstDay = Calendar.getInstance()
    calendar.time = Date()
    firstDay.add(Calendar.DATE, (calendar.get(Calendar.DAY_OF_WEEK) - 1) * -1)
    return firstDay.time
}

fun weekLastDate(): Date {
    val calendar: Calendar = Calendar.getInstance()
    val lastDay = Calendar.getInstance()
    calendar.time = Date()
    lastDay.add(Calendar.DATE, 7 - calendar.get(Calendar.DAY_OF_WEEK))
    return lastDay.time
}