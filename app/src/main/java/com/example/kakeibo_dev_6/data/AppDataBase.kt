package com.example.kakeibo_dev_6.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.kakeibo_dev_6.data.dao.CategorizeExpenditureItemDao
import com.example.kakeibo_dev_6.data.dao.CategoryDao
import com.example.kakeibo_dev_6.data.dao.ExpenditureItemDao
import com.example.kakeibo_dev_6.data.dao.ExpenditureItemJoinCategoryDao
import com.example.kakeibo_dev_6.data.entity.CategorizeExpenditureItem
import com.example.kakeibo_dev_6.data.entity.ExpenditureItem
import com.example.kakeibo_dev_6.data.entity.Category
import com.example.kakeibo_dev_6.data.entity.ExpenditureItemJoinCategory

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