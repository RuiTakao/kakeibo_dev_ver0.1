package com.example.kakeibo_dev_6

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Category(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var name: String,
    var order: Int
)