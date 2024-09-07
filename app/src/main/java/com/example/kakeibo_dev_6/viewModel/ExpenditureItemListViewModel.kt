package com.example.kakeibo_dev_6.viewModel

import androidx.lifecycle.ViewModel
import com.example.kakeibo_dev_6.dao.CategorizeExpenditureItemDao
import com.example.kakeibo_dev_6.entity.CategorizeExpenditureItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

@HiltViewModel
class ExpenditureItemListViewModel @Inject constructor(
    private val categorizeExpenditureItemDao: CategorizeExpenditureItemDao
): ViewModel() {
    fun categorizeExpenditureItem(
        firstDay: String,
        lastDay: String
    ): Flow<List<CategorizeExpenditureItem>> {
        return categorizeExpenditureItemDao.categorizeExpenditureItem(
            firstDay = firstDay,
            lastDay = lastDay
        ).distinctUntilChanged()
    }
}