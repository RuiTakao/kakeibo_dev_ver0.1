package com.kakeibo.kakeibo.presentation.expenditure_item.expenditure_item_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kakeibo.kakeibo.domain.model.ExpenditureItem
import com.kakeibo.kakeibo.domain.repository.CategoryDao
import com.kakeibo.kakeibo.domain.repository.ExpenditureItemDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExpenditureItemDetailViewModel @Inject constructor(
    private val expenditureItemDao: ExpenditureItemDao,
    categoryDao: CategoryDao
) : ViewModel() {

    /**
     * 支出項目取得
     *
     * @param id Int
     *
     * @return Flow<ExpenditureItem>
     */
    fun getExpenditureItem(id: Int): Flow<ExpenditureItem> {
        return expenditureItemDao.loadExpenditureItem(id = id).distinctUntilChanged()
    }

    // 取得した支出項目保存
    var expenditureItem: ExpenditureItem? = null

    /**
     * 支出項目削除
     *
     * @param expendItem ExpenditureItem
     *
     * @return Unit
     */
    fun deleteExpenditureItem(expendItem: ExpenditureItem) {
        viewModelScope.launch {
            expenditureItemDao.deleteExpenditureItem(expendItem)
        }
    }

    // カテゴリー一覧取得
    val categoryList = categoryDao.loadAllCategories().distinctUntilChanged()
}