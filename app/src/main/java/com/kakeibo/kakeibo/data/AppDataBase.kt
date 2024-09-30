package com.kakeibo.kakeibo.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kakeibo.kakeibo.domain.model.CategorizeExpenditureItem
import com.kakeibo.kakeibo.domain.model.Category
import com.kakeibo.kakeibo.domain.model.ExpenditureItem
import com.kakeibo.kakeibo.domain.model.ExpenditureItemJoinCategory
import com.kakeibo.kakeibo.domain.repository.CategorizeExpenditureItemDao
import com.kakeibo.kakeibo.domain.repository.CategoryDao
import com.kakeibo.kakeibo.domain.repository.ExpenditureItemDao
import com.kakeibo.kakeibo.domain.repository.ExpenditureItemJoinCategoryDao

@Database(
    entities = [
        ExpenditureItem::class,
        Category::class
    ],
    views = [
        CategorizeExpenditureItem::class,
        ExpenditureItemJoinCategory::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDataBase : RoomDatabase() {

    /**
     * 支出項目テーブル
     *
     * @return ExpenditureItemDao
     */
    abstract fun expenditureItemDao(): ExpenditureItemDao

    /**
     * カテゴリーテーブル
     *
     * @return CategoryDao
     */
    abstract fun categoryDao(): CategoryDao

    /**
     * カテゴリーJoin支出項目ビュー
     *
     * @return CategorizeExpenditureItemDao
     */
    abstract fun categorizeExpenditureItemDao(): CategorizeExpenditureItemDao

    /**
     * 支出項目Joinカテゴリービュー
     *
     * @return ExpenditureItemJoinCategoryDao
     */
    abstract fun expenditureItemJoinCategoryDao(): ExpenditureItemJoinCategoryDao
}