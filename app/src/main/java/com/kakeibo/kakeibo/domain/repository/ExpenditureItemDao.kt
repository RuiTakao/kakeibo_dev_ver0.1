package com.kakeibo.kakeibo.domain.repository

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.kakeibo.kakeibo.domain.model.ExpenditureItem
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenditureItemDao {

    /**
     * 支出項目登録
     *
     * @param expenditureItem ExpenditureItem
     *
     * @return Unit
     */
    @Insert
    suspend fun insertExpenditureItem(expenditureItem: ExpenditureItem)

    /**
     * 支出項目全件取得
     *
     * @return Flow<List<ExpenditureItem>>
     */
    @Query("SELECT * FROM ExpenditureItem")
    fun loadAllExpenditureItems(): Flow<List<ExpenditureItem>>

    /**
     * 支出項目、idをキーに一件取得
     *
     * @param id Int
     *
     * @return Flow<ExpenditureItem>
     */
    @Query("SELECT * FROM ExpenditureItem WHERE id = :id")
    fun loadExpenditureItem(id: Int): Flow<ExpenditureItem>

    /**
     * 支出項目、カテゴリーIDをキーに一件取得
     *
     * @param categoryId Int
     *
     * @return Flow<ExpenditureItem>
     */
    @Query("SELECT * FROM ExpenditureItem WHERE categoryId = :categoryId")
    fun isUsedCategory(categoryId: Int): Flow<List<ExpenditureItem>>

    /**
     * 支出日でグループ化した支出項目取得、カテゴリーIDをキーに降順で取得
     *
     * @param startDate String
     * @param endDate String
     * @param categoryId Int
     *
     * @return Flow<List<ExpenditureItem>>
     */
    @Query(
        "" +
                "SELECT * FROM ExpenditureItem WHERE CASE " +
                "WHEN :categoryId = 0 " +
                "THEN strftime('%Y-%m-%d', payDate) BETWEEN :startDate AND :endDate " +
                "ELSE strftime('%Y-%m-%d', payDate) BETWEEN :startDate AND :endDate AND categoryId = :categoryId " +
                "END " +
                "GROUP BY payDate " +
                "ORDER BY payDate DESC " +
                ""
    )
    fun gropePayDateDesc(
        startDate: String,
        endDate: String,
        categoryId: Int
    ): Flow<List<ExpenditureItem>>

    /**
     * 支出日でグループ化した支出項目取得、カテゴリーIDをキーに降順で取得
     *
     * @param startDate String
     * @param endDate String
     * @param categoryId Int
     *
     * @return Flow<List<ExpenditureItem>>
     */
    @Query(
        "" +
                "SELECT * FROM ExpenditureItem WHERE CASE " +
                "WHEN :categoryId = 0 " +
                "THEN strftime('%Y-%m-%d', payDate) BETWEEN :startDate AND :endDate " +
                "ELSE strftime('%Y-%m-%d', payDate) BETWEEN :startDate AND :endDate AND categoryId = :categoryId " +
                "END " +
                "GROUP BY payDate " +
                "ORDER BY payDate ASC " +
                ""
    )
    fun gropePayDateAsc(
        startDate: String,
        endDate: String,
        categoryId: Int
    ): Flow<List<ExpenditureItem>>

    /**
     * 支出項目更新
     *
     * @param expenditureItem ExpenditureItem
     *
     * @return Unit
     */
    @Update
    suspend fun updateExpenditureItem(expenditureItem: ExpenditureItem)

    /**
     * 支出項目削除
     *
     * @param expenditureItem ExpenditureItem
     *
     * @return Unit
     */
    @Delete
    suspend fun deleteExpenditureItem(expenditureItem: ExpenditureItem)
}