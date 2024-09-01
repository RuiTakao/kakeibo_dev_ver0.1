package com.example.kakeibo_dev_6

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.kakeibo_dev_6.dao.CategoryDao
import com.example.kakeibo_dev_6.dao.ExpenditureItemDao
import com.example.kakeibo_dev_6.dao.ExpenditureItemWithCategoryDao
import com.example.kakeibo_dev_6.entity.ExpenditureItem
import com.example.kakeibo_dev_6.entity.ExpenditureItemWithCategory
import com.example.kakeibo_dev_6.entity.Category

@Database(
    entities = [
        ExpenditureItem::class,
        Category::class
    ],
    views = [ExpenditureItemWithCategory::class],
    version = 1,
    exportSchema = false
)
abstract class AppDataBase: RoomDatabase() {
    abstract fun expenditureItemDao(): ExpenditureItemDao
    abstract fun categoryDao(): CategoryDao
    abstract fun expenditureItemWithCategoryDao(): ExpenditureItemWithCategoryDao
}