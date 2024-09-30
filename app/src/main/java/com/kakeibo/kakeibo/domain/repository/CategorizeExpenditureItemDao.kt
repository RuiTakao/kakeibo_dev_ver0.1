package com.kakeibo.kakeibo.domain.repository

import androidx.room.Dao
import androidx.room.Query
import androidx.room.RewriteQueriesToDropUnusedColumns
import com.kakeibo.kakeibo.domain.model.CategorizeExpenditureItem
import kotlinx.coroutines.flow.Flow

@Dao
interface CategorizeExpenditureItemDao {

    /**
     * 支出項目一覧をカテゴリー毎に取得
     *
     * @param startDate String
     * @param endDate String
     *
     * @return Flow<List<CategorizeExpenditureItem>>
     */
    @RewriteQueriesToDropUnusedColumns
    @Query(
        "" +
                "SELECT id, categoryName, SUM(price) AS price, COUNT(categoryId) AS categoryId, payDate, categoryOrder FROM CategorizeExpenditureItem " +
                "WHERE strftime('%Y-%m-%d', payDate) BETWEEN :startDate AND :endDate " +
                "GROUP BY categoryName " +
                "ORDER BY categoryOrder DESC" +
                ""
    )
    fun categorizeExpenditureItem(
        startDate: String,
        endDate: String
    ): Flow<List<CategorizeExpenditureItem>>
}