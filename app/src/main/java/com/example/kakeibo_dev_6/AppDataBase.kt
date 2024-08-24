package com.example.kakeibo_dev_6

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        ExpendItem::class,
        Category::class
    ],
    views = [GroupCategory::class],
    version = 1,
    exportSchema = false
)
abstract class AppDataBase: RoomDatabase() {
    abstract fun expendItemDao(): ExpendItemDao
    abstract fun categoryDao(): CategoryDao
    abstract fun groupCategoryDao(): GroupCategoryDao
}