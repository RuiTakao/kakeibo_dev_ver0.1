package com.example.kakeibo_dev_6.entity

import androidx.room.DatabaseView
import androidx.room.Embedded
import androidx.room.Relation

@DatabaseView(
    viewName = "GroupCategory",
    value = """
        SELECT * From category INNER JOIN expenditem ON category.id = expenditem.category_id
    """
)
data class GroupCategory (
    val id: Int,
    val name: String,
    val price: String,
    val content: String,
    val payDate: String,
    val category_id: Int
)