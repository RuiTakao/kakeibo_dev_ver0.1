package com.example.kakeibo_dev_6.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.kakeibo_dev_6.entity.ExpenditureItem
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenditureItemDao {

    @Insert
    suspend fun insertExpenditureItem(expenditureItem: ExpenditureItem)

    @Query("SELECT * FROM ExpenditureItem")
    fun loadAllExpenditureItems(): Flow<List<ExpenditureItem>>

    @Query("SELECT * FROM ExpenditureItem WHERE id = :id")
    fun loadExpenditureItem(id: Int): Flow<ExpenditureItem>

    @Update
    suspend fun updateExpenditureItem(expenditureItem: ExpenditureItem)

    @Delete
    suspend fun deleteExpenditureItem(expenditureItem: ExpenditureItem)
}