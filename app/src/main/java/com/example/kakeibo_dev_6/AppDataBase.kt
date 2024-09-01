package com.example.kakeibo_dev_6

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.kakeibo_dev_6.dao.CategoryDao
import com.example.kakeibo_dev_6.dao.ExpendItemDao
import com.example.kakeibo_dev_6.dao.GroupCategoryDao
import com.example.kakeibo_dev_6.entity.ExpendItem
import com.example.kakeibo_dev_6.entity.GroupCategory
import com.example.kakeibo_dev_6.entity.Category

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