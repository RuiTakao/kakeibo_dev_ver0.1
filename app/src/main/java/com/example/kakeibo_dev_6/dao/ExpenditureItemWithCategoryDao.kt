package com.example.kakeibo_dev_6.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.RewriteQueriesToDropUnusedColumns
import com.example.kakeibo_dev_6.entity.ExpenditureItemWithCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenditureItemWithCategoryDao {

    @RewriteQueriesToDropUnusedColumns
    @Query("" +
            "SELECT * FROM ExpenditureItemWithCategory WHERE CASE " +
            "WHEN :category = 0 " +
            "THEN strftime('%Y-%m-%d', payDate) BETWEEN :firstDay AND :lastDay " +
            "ELSE strftime('%Y-%m-%d', payDate) BETWEEN :firstDay AND :lastDay AND categoryId = :category " +
            "END " +
            "ORDER BY payDate DESC " +
            "")
    fun expAll(firstDay: String, lastDay: String, category: Int): Flow<List<ExpenditureItemWithCategory>>

    @RewriteQueriesToDropUnusedColumns
    @Query("" +
            "SELECT * FROM ExpenditureItemWithCategory WHERE CASE " +
            "WHEN :category = 0 " +
            "THEN strftime('%Y-%m-%d', payDate) BETWEEN :firstDay AND :lastDay " +
            "ELSE strftime('%Y-%m-%d', payDate) BETWEEN :firstDay AND :lastDay AND categoryId = :category " +
            "END " +
            "ORDER BY payDate ASC " +
            "")
    fun expAllAsc(firstDay: String, lastDay: String, category: Int): Flow<List<ExpenditureItemWithCategory>>

    @RewriteQueriesToDropUnusedColumns
    @Query("" +
            "SELECT categoryName, SUM(price) AS price, content, COUNT(categoryId) AS categoryId, payDate, expenditureId FROM ExpenditureItemWithCategory " +
            "WHERE strftime('%Y-%m-%d', payDate) BETWEEN :firstDay AND :lastDay " +
            "GROUP BY categoryName" +
            "")
    fun getAll(firstDay: String, lastDay: String): Flow<List<ExpenditureItemWithCategory>>

    @RewriteQueriesToDropUnusedColumns
    @Query("SELECT * FROM ExpenditureItemWithCategory WHERE expenditureId = :id")
    fun OneOfGroupCategory(id: Int): Flow<ExpenditureItemWithCategory>
}