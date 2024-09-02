package com.example.kakeibo_dev_6.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.RewriteQueriesToDropUnusedColumns
import com.example.kakeibo_dev_6.entity.CategorizeExpenditureItem
import kotlinx.coroutines.flow.Flow

@Dao
interface CategorizeExpenditureItemDao {

    @RewriteQueriesToDropUnusedColumns
    @Query(
        "" +
                "SELECT id, categoryName, SUM(price) AS price, COUNT(categoryId) AS categoryId, payDate FROM CategorizeExpenditureItem " +
                "WHERE strftime('%Y-%m-%d', payDate) BETWEEN :firstDay AND :lastDay " +
                "GROUP BY categoryName" +
                ""
    )
    fun categorizeExpenditureItem(firstDay: String, lastDay: String): Flow<List<CategorizeExpenditureItem>>

//    @RewriteQueriesToDropUnusedColumns
//    @Query("SELECT * FROM ExpenditureItemWithCategory WHERE expenditureId = :id")
//    fun OneOfGroupCategory(id: Int): Flow<ExpenditureItemWithCategory>
}