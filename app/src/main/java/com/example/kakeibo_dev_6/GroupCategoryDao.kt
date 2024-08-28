package com.example.kakeibo_dev_6

import androidx.room.Dao
import androidx.room.Query
import androidx.room.RewriteQueriesToDropUnusedColumns
import kotlinx.coroutines.flow.Flow

@Dao
interface GroupCategoryDao {

    @RewriteQueriesToDropUnusedColumns
    @Query("SELECT * FROM GroupCategory ORDER BY payDate DESC")
    fun expAll(): Flow<List<GroupCategory>>

    @RewriteQueriesToDropUnusedColumns
    @Query("SELECT name, SUM(price) AS price, content, COUNT(id) AS id, payDate FROM GroupCategory WHERE strftime('%Y-%m-%d', payDate) BETWEEN :firstDay AND :lastDay GROUP BY name")
    fun getAll(firstDay: String, lastDay: String): Flow<List<GroupCategory>>

//    @RewriteQueriesToDropUnusedColumns
//    @Query("SELECT name, SUM(price) AS price, content, COUNT(id) AS id, payDate FROM GroupCategory GROUP BY name")
//    fun getAll(): Flow<List<GroupCategory>>
}