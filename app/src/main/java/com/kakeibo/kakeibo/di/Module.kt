package com.kakeibo.kakeibo.di

import android.content.Context
import androidx.room.Room
import com.kakeibo.kakeibo.data.AppDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object Module {

    /**
     * データベース作成
     *
     * @param context Context
     *
     * @return AppDataBase
     */
    @Provides
    fun provideDataBase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(context, AppDataBase::class.java, "main.db").build()

    /**
     * 支出項目テーブル作成
     *
     * @param db AppDataBase
     *
     * @return expenditureItemDao
     */
    @Provides
    fun provideExpenditureItemDao(db: AppDataBase) = db.expenditureItemDao()

    /**
     * カテゴリテーブル作成
     *
     * @param db AppDataBase
     *
     * @return categoryDao
     */
    @Provides
    fun provideCategoryDao(db: AppDataBase) = db.categoryDao()

    /**
     * カテゴリーJoin支出項目ビュー作成
     *
     * @param db AppDataBase
     *
     * @return categorizeExpenditureItemDao
     */
    @Provides
    fun provideCategorizeExpenditureItemDao(db: AppDataBase) = db.categorizeExpenditureItemDao()

    /**
     * 支出項目Joinカテゴリービュー作成
     *
     * @param db AppDataBase
     *
     * @return expenditureItemJoinCategoryDao
     */
    @Provides
    fun provideExpenditureItemJoinCategoryDao(db: AppDataBase) = db.expenditureItemJoinCategoryDao()
}