package com.example.kakeibo_dev_6

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kakeibo_dev_6.dao.CategoryDao
import com.example.kakeibo_dev_6.dao.ExpendItemDao
import com.example.kakeibo_dev_6.dao.GroupCategoryDao
import com.example.kakeibo_dev_6.entity.ExpendItem
import com.example.kakeibo_dev_6.entity.GroupCategory
import com.example.kakeibo_dev_6.entity.Category
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val expendItemDao: ExpendItemDao,
    private val categoryDao: CategoryDao,
    private val groupCategoryDao: GroupCategoryDao
) : ViewModel() {
    var payDate by mutableStateOf("")
    var category_id by mutableStateOf("")
    var content by mutableStateOf("")
    var price by mutableStateOf("")
    var name by mutableStateOf("")
    var order by mutableStateOf(0)

    val expendItem = expendItemDao.loadAllExpendItems().distinctUntilChanged()
    val category = categoryDao.loadAllCategories().distinctUntilChanged()

    fun detailExpendItem(
        firstDay: String,
        lastDay: String,
        categoryId: Int,
        sort: Boolean
    ): Flow<List<GroupCategory>> {
        return if (sort) {
            groupCategoryDao.expAllAsc(firstDay = firstDay, lastDay = lastDay, category = categoryId)
                .distinctUntilChanged()
        } else {
            groupCategoryDao.expAll(firstDay = firstDay, lastDay = lastDay, category = categoryId).distinctUntilChanged()
        }
    }

    fun groupeExpendItem(firstDay: String, lastDay: String): Flow<List<GroupCategory>> {
        val groupCategory =
            groupCategoryDao.getAll(firstDay = firstDay, lastDay = lastDay).distinctUntilChanged()
        return groupCategory
    }

    fun createExpendItem() {
        viewModelScope.launch {
            val newExpendItem = ExpendItem(
                payDate = payDate,
                category_id = category_id,
                content = content,
                price = price
            )
            expendItemDao.insertExpendItem(newExpendItem)
        }
    }

    fun createCategory() {
        viewModelScope.launch {
            val newCategory = Category(name = name, order = order)
            categoryDao.insertCategory(newCategory)
        }
    }

    fun setEditingCategory(id: Int): Flow<Category> {
        val oneOfCategory = categoryDao.getOneOfCategory(id = id).distinctUntilChanged()
        return oneOfCategory
    }

    fun setEditingExpendItem(id: Int): Flow<ExpendItem> {
        val oneOfExpendItem = expendItemDao.getOneOfExpendItem(id = id).distinctUntilChanged()
        return oneOfExpendItem
    }

    fun OneOfGroupCategory(id: Int): Flow<GroupCategory> {
        return groupCategoryDao.OneOfGroupCategory(id = id).distinctUntilChanged()
    }

    var setGroupCategory: GroupCategory? = null

    var editingCategory: Category? = null

    var editingExpendItem: ExpendItem? = null

    fun updateCategory() {
        editingCategory?.let { category ->
            viewModelScope.launch {
                category.name = name
                categoryDao.updateCategory(category)
            }
        }
    }

    fun updateExpendItem() {
        editingExpendItem?.let { expendItem ->
            viewModelScope.launch {
                expendItem.content = content
                expendItemDao.updateExpendItem(expendItem)
            }
        }
    }

    fun deleteCategory(category: Category) {
        viewModelScope.launch {
            categoryDao.deleteCategory(category)
        }
    }

    fun deleteExpendItem(expendItem: ExpendItem) {
        viewModelScope.launch {
            expendItemDao.deleteExpendItem(expendItem)
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