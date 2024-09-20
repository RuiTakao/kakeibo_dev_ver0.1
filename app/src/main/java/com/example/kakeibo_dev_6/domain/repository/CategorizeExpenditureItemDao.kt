package com.example.kakeibo_dev_6.domain.repository

import androidx.room.Dao
import androidx.room.Query
import androidx.room.RewriteQueriesToDropUnusedColumns
import com.example.kakeibo_dev_6.domain.model.CategorizeExpenditureItem
import kotlinx.coroutines.flow.Flow

@Dao
interface CategorizeExpenditureItemDao {

    @RewriteQueriesToDropUnusedColumns
    @Query(
        "" +
                "SELECT id, categoryName, SUM(price) AS price, COUNT(categoryId) AS categoryId, payDate, categoryOrder FROM CategorizeExpenditureItem " +
                "WHERE strftime('%Y-%m-%d', payDate) BETWEEN :firstDay AND :lastDay " +
                "GROUP BY categoryName " +
                "ORDER BY categoryOrder DESC" +
                ""
    )
    fun categorizeExpenditureItem(
        firstDay: String,
        lastDay: String
    ): Flow<List<CategorizeExpenditureItem>>
}