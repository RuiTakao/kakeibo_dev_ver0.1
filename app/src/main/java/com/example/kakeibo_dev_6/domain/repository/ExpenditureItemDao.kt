package com.example.kakeibo_dev_6.domain.repository

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.kakeibo_dev_6.domain.model.ExpenditureItem
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenditureItemDao {

    @Insert
    suspend fun insertExpenditureItem(expenditureItem: ExpenditureItem)

    @Query("SELECT * FROM ExpenditureItem")
    fun loadAllExpenditureItems(): Flow<List<ExpenditureItem>>

    @Query("SELECT * FROM ExpenditureItem WHERE id = :id")
    fun loadExpenditureItem(id: Int): Flow<ExpenditureItem>

    @Query("SELECT * FROM ExpenditureItem WHERE categoryId = :categoryId")
    fun isUsedCategory(categoryId: Int): Flow<List<ExpenditureItem>>

    @Query(
        "" +
                "SELECT * FROM ExpenditureItem WHERE CASE " +
                "WHEN :categoryId = 0 " +
                "THEN strftime('%Y-%m-%d', payDate) BETWEEN :firstDay AND :lastDay " +
                "ELSE strftime('%Y-%m-%d', payDate) BETWEEN :firstDay AND :lastDay AND categoryId = :categoryId " +
                "END " +
                "GROUP BY payDate " +
                "ORDER BY payDate DESC " +
                ""
    )
    fun gropePayDateDesc(
        firstDay: String,
        lastDay: String,
        categoryId: Int
    ): Flow<List<ExpenditureItem>>

    @Query(
        "" +
                "SELECT * FROM ExpenditureItem WHERE CASE " +
                "WHEN :categoryId = 0 " +
                "THEN strftime('%Y-%m-%d', payDate) BETWEEN :firstDay AND :lastDay " +
                "ELSE strftime('%Y-%m-%d', payDate) BETWEEN :firstDay AND :lastDay AND categoryId = :categoryId " +
                "END " +
                "GROUP BY payDate " +
                "ORDER BY payDate ASC " +
                ""
    )
    fun gropePayDateAsc(
        firstDay: String,
        lastDay: String,
        categoryId: Int
    ): Flow<List<ExpenditureItem>>

    @Update
    suspend fun updateExpenditureItem(expenditureItem: ExpenditureItem)

    @Delete
    suspend fun deleteExpenditureItem(expenditureItem: ExpenditureItem)
}