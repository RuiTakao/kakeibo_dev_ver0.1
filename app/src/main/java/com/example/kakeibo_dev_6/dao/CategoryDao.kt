package com.example.kakeibo_dev_6.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.kakeibo_dev_6.entity.Category
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Insert
    suspend fun insertCategory(category: Category)

    @Query("SELECT * FROM Category")
    fun loadAllCategories(): Flow<List<Category>>

    @Query("SELECT * FROM Category WHERE categoryId = :id")
    fun getOneOfCategory(id: Int): Flow<Category>

    @Update
    suspend fun updateCategory(category: Category)

    @Delete
    suspend fun deleteCategory(category: Category)
}