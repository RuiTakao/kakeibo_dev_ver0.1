package com.example.kakeibo_dev_6

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Insert
    suspend fun insertCategory(category: Category)

    @Query("SELECT * FROM Category")
    fun loadAllCategories(): Flow<List<Category>>

    @Query("SELECT * FROM Category WHERE id = :id")
    fun getOneOfCategory(id: Int): Flow<Category>

    @Update
    suspend fun updateCategory(category: Category)

    @Delete
    suspend fun deleteCategory(category: Category)

//    @Query("SELECT * FROM Category, ExpendItem")
//    fun loadAllGroupeExpend(): Flow<List<GroupCategory>>
}