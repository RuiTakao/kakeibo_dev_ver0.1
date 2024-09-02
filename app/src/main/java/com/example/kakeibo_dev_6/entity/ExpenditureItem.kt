package com.example.kakeibo_dev_6.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ExpenditureItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var payDate: String,
    var categoryId: String,
    var content: String,
    var price: String
)