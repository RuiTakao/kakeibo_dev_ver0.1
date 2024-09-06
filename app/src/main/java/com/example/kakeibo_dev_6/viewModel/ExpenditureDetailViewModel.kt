package com.example.kakeibo_dev_6.viewModel

import androidx.lifecycle.ViewModel
import com.example.kakeibo_dev_6.dao.ExpenditureItemJoinCategoryDao
import com.example.kakeibo_dev_6.entity.ExpenditureItemJoinCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

@HiltViewModel
class ExpenditureDetailViewModel @Inject constructor(
    private val expenditureItemJoinCategoryDao: ExpenditureItemJoinCategoryDao
): ViewModel()  {
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
}