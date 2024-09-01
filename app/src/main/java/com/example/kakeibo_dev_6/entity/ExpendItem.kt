package com.example.kakeibo_dev_6.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity
data class ExpendItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var payDate: String,
    var category_id: String,
    var content: String,
    var price: String
)