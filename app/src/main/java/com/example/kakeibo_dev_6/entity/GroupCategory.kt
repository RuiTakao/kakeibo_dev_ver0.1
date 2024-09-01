package com.example.kakeibo_dev_6.entity

import androidx.room.DatabaseView
import androidx.room.Embedded
import androidx.room.Relation

@DatabaseView(
    viewName = "GroupCategory",
    value = """
        SELECT * From category INNER JOIN expenditureitem ON category.categoryId = expenditureitem.categoryId
    """
)
data class GroupCategory (
    val categoryName: String,
    val price: String,
    val content: String,
    val payDate: String,
    val categoryId: Int,
    val expenditureId: Int
)