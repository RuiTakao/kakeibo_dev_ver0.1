package com.example.kakeibo_dev_6

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpendItemDao {

    @Insert
    suspend fun insertExpendItem(expendItem: ExpendItem)

    @Query("SELECT * FROM ExpendItem")
    fun loadAllExpendItems(): Flow<List<ExpendItem>>

    @Query("SELECT * FROM ExpendItem WHERE id = :id")
    fun getOneOfExpendItem(id: Int): Flow<ExpendItem>

    @Update
    suspend fun updateExpendItem(expendItem: ExpendItem)

    @Delete
    suspend fun deleteExpendItem(expendItem: ExpendItem)
}