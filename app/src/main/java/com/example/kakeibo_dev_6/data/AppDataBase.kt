package com.example.kakeibo_dev_6.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.kakeibo_dev_6.domain.repository.CategorizeExpenditureItemDao
import com.example.kakeibo_dev_6.domain.repository.CategoryDao
import com.example.kakeibo_dev_6.domain.repository.ExpenditureItemDao
import com.example.kakeibo_dev_6.domain.repository.ExpenditureItemJoinCategoryDao
import com.example.kakeibo_dev_6.domain.model.CategorizeExpenditureItem
import com.example.kakeibo_dev_6.domain.model.ExpenditureItem
import com.example.kakeibo_dev_6.domain.model.Category
import com.example.kakeibo_dev_6.domain.model.ExpenditureItemJoinCategory

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
    abstract fun expenditureItemDao(): ExpenditureItemDao
    abstract fun categoryDao(): CategoryDao
    abstract fun categorizeExpenditureItemDao(): CategorizeExpenditureItemDao
    abstract fun expenditureItemJoinCategoryDao(): ExpenditureItemJoinCategoryDao
}