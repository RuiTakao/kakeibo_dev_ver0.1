package com.example.kakeibo_dev_6

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.kakeibo_dev_6.dao.CategorizeExpenditureItemDao
import com.example.kakeibo_dev_6.dao.CategoryDao
import com.example.kakeibo_dev_6.dao.ExpenditureItemDao
import com.example.kakeibo_dev_6.dao.ExpenditureItemJoinCategoryDao
import com.example.kakeibo_dev_6.entity.CategorizeExpenditureItem
import com.example.kakeibo_dev_6.entity.ExpenditureItem
import com.example.kakeibo_dev_6.entity.Category
import com.example.kakeibo_dev_6.entity.ExpenditureItemJoinCategory

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