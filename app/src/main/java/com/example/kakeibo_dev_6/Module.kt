package com.example.kakeibo_dev_6

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object Module {

    // データベース作成
    @Provides
    fun provideDataBase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(context, AppDataBase::class.java, "main.db").build()

    // 支出項目テーブル作成
    @Provides
    fun provideDao(db: AppDataBase) = db.expendItemDao()

    // カテゴリテーブル作成
    @Provides
    fun provideDaos(db: AppDataBase) = db.categoryDao()

    @Provides
    fun provideDaoss(db: AppDataBase) = db.groupCategoryDao()
}