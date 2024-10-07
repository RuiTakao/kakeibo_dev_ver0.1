package com.kakeibo.kakeibo_dev_6.domain.repository

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.kakeibo.kakeibo_dev_6.domain.model.Category
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    /**
     * カテゴリー登録
     *
     * @param category Category
     *
     * @return Unit
     */
    @Insert
    suspend fun insertCategory(category: Category)

    /**
     * カテゴリー全件取得
     *
     * @return Flow<List<Category>>
     */
    @Query("SELECT * FROM Category ORDER BY categoryOrder DESC")
    fun loadAllCategories(): Flow<List<Category>>

    /**
     * カテゴリー、idをキーに一件取得
     *
     * @param id Int
     *
     * @return Flow<Category>
     */
    @Query("SELECT * FROM Category WHERE id = :id")
    fun getOneOfCategory(id: Int): Flow<Category>

    /**
     * カテゴリーの最前列のカテゴリーの並び順番号を取得
     *
     * @return Flow<Category>
     */
    @Query("SELECT MAX(categoryOrder) AS id, MAX(categoryOrder) AS categoryName, MAX(categoryOrder) AS categoryOrder FROM Category")
    fun maxOrderCategory(): Flow<Category>

    /**
     * カテゴリー更新
     *
     * @param category Category
     *
     * @return Unit
     */
    @Update
    suspend fun updateCategory(category: Category)

    /**
     * カテゴリー削除
     *
     * @param category Category
     *
     * @return Unit
     */
    @Delete
    suspend fun deleteCategory(category: Category)
}