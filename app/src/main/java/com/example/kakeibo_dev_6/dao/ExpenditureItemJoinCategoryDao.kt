package com.example.kakeibo_dev_6.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.RewriteQueriesToDropUnusedColumns
import com.example.kakeibo_dev_6.entity.ExpenditureItemJoinCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenditureItemJoinCategoryDao {
    @RewriteQueriesToDropUnusedColumns
    @Query(
        "" +
                "SELECT * FROM ExpenditureItemJoinCategory WHERE CASE " +
                "WHEN :category = 0 " +
                "THEN strftime('%Y-%m-%d', payDate) BETWEEN :firstDay AND :lastDay " +
                "ELSE strftime('%Y-%m-%d', payDate) BETWEEN :firstDay AND :lastDay AND categoryId = :category " +
                "END " +
                "ORDER BY payDate DESC " +
                ""
    )
    fun expAll(
        firstDay: String,
        lastDay: String,
        category: Int
    ): Flow<List<ExpenditureItemJoinCategory>>

    @RewriteQueriesToDropUnusedColumns
    @Query(
        "" +
                "SELECT * FROM ExpenditureItemJoinCategory WHERE CASE " +
                "WHEN :category = 0 " +
                "THEN strftime('%Y-%m-%d', payDate) BETWEEN :firstDay AND :lastDay " +
                "ELSE strftime('%Y-%m-%d', payDate) BETWEEN :firstDay AND :lastDay AND categoryId = :category " +
                "END " +
                "ORDER BY payDate ASC " +
                ""
    )
    fun expAllAsc(
        firstDay: String,
        lastDay: String,
        category: Int
    ): Flow<List<ExpenditureItemJoinCategory>>
}