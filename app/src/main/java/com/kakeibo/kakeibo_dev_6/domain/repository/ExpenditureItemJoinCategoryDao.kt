package com.kakeibo.kakeibo_dev_6.domain.repository

import androidx.room.Dao
import androidx.room.Query
import androidx.room.RewriteQueriesToDropUnusedColumns
import com.kakeibo.kakeibo_dev_6.domain.model.ExpenditureItemJoinCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenditureItemJoinCategoryDao {

    /**
     * 支出項目一覧　Join カテゴリー
     * 並び順、支出日降順
     *
     * @param startDate String
     * @param endDate String
     * @param categoryId Int
     *
     * @return Flow<List<ExpenditureItemJoinCategory>>
     */
    @RewriteQueriesToDropUnusedColumns
    @Query(
        "" +
                "SELECT * FROM ExpenditureItemJoinCategory WHERE CASE " +
                "WHEN :categoryId = 0 " +
                "THEN strftime('%Y-%m-%d', payDate) BETWEEN :startDate AND :endDate " +
                "ELSE strftime('%Y-%m-%d', payDate) BETWEEN :startDate AND :endDate AND categoryId = :categoryId " +
                "END " +
                "ORDER BY payDate DESC " +
                ""
    )
    fun loadAllExpenditureItemOrderDesc(
        startDate: String,
        endDate: String,
        categoryId: Int
    ): Flow<List<ExpenditureItemJoinCategory>>

    /**
     * 支出項目一覧　Join カテゴリー
     * 並び順、支出日昇順
     *
     * @param startDate String
     * @param endDate String
     * @param categoryId Int
     *
     * @return Flow<List<ExpenditureItemJoinCategory>>
     */
    @RewriteQueriesToDropUnusedColumns
    @Query(
        "" +
                "SELECT * FROM ExpenditureItemJoinCategory WHERE CASE " +
                "WHEN :categoryId = 0 " +
                "THEN strftime('%Y-%m-%d', payDate) BETWEEN :startDate AND :endDate " +
                "ELSE strftime('%Y-%m-%d', payDate) BETWEEN :startDate AND :endDate AND categoryId = :categoryId " +
                "END " +
                "ORDER BY payDate ASC " +
                ""
    )
    fun loadAllExpenditureItemOrderAsc(
        startDate: String,
        endDate: String,
        categoryId: Int
    ): Flow<List<ExpenditureItemJoinCategory>>
}