package com.kakeibo.kakeibo.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Category(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var categoryName: String,
    var categoryOrder: Int
)