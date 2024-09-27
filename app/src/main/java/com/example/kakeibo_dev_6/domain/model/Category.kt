package com.example.kakeibo_dev_6.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Category(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var categoryName: String,
    var categoryOrder: Int
)