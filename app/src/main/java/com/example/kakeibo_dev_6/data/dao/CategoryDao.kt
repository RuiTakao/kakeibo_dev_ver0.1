package com.example.kakeibo_dev_6.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.kakeibo_dev_6.data.entity.Category
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Insert
    suspend fun insertCategory(category: Category)

    @Query("SELECT * FROM Category ORDER BY categoryOrder DESC")
    fun loadAllCategories(): Flow<List<Category>>

    @Query("SELECT * FROM Category WHERE id = :id")
    fun getOneOfCategory(id: Int): Flow<Category>

    @Query("SELECT MAX(categoryOrder) AS id, MAX(categoryOrder) AS categoryName, MAX(categoryOrder) AS categoryOrder FROM Category")
    fun maxOrderCategory(): Flow<Category>

    @Update
    suspend fun updateCategory(category: Category)

    @Delete
    suspend fun deleteCategory(category: Category)
}