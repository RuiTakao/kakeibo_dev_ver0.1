package com.example.kakeibo_dev_6.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.RewriteQueriesToDropUnusedColumns
import com.example.kakeibo_dev_6.entity.GroupCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface GroupCategoryDao {

    @RewriteQueriesToDropUnusedColumns
    @Query("" +
            "SELECT * FROM GroupCategory WHERE CASE " +
            "WHEN :category = 0 " +
            "THEN strftime('%Y-%m-%d', payDate) BETWEEN :firstDay AND :lastDay " +
            "ELSE strftime('%Y-%m-%d', payDate) BETWEEN :firstDay AND :lastDay AND categoryId = :category " +
            "END " +
            "ORDER BY payDate DESC " +
            "")
    fun expAll(firstDay: String, lastDay: String, category: Int): Flow<List<GroupCategory>>

    @RewriteQueriesToDropUnusedColumns
    @Query("" +
            "SELECT * FROM GroupCategory WHERE CASE " +
            "WHEN :category = 0 " +
            "THEN strftime('%Y-%m-%d', payDate) BETWEEN :firstDay AND :lastDay " +
            "ELSE strftime('%Y-%m-%d', payDate) BETWEEN :firstDay AND :lastDay AND categoryId = :category " +
            "END " +
            "ORDER BY payDate ASC " +
            "")
    fun expAllAsc(firstDay: String, lastDay: String, category: Int): Flow<List<GroupCategory>>

    @RewriteQueriesToDropUnusedColumns
    @Query("" +
            "SELECT categoryName, SUM(price) AS price, content, COUNT(categoryId) AS categoryId, payDate, expenditureId FROM GroupCategory " +
            "WHERE strftime('%Y-%m-%d', payDate) BETWEEN :firstDay AND :lastDay " +
            "GROUP BY categoryName" +
            "")
    fun getAll(firstDay: String, lastDay: String): Flow<List<GroupCategory>>

    @RewriteQueriesToDropUnusedColumns
    @Query("SELECT * FROM GroupCategory WHERE expenditureId = :id")
    fun OneOfGroupCategory(id: Int): Flow<GroupCategory>
}