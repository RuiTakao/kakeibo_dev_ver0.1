package com.example.kakeibo_dev_6

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface GroupCategoryDao {

    @Query("SELECT * FROM GroupCategory")
    fun getAll(): Flow<List<GroupCategory>>
}