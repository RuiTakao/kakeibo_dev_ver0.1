package com.example.kakeibo_dev_6.entity

import androidx.room.DatabaseView

@DatabaseView(
    viewName = "ExpenditureItemWithCategory",
    value = """
        SELECT * From category INNER JOIN expenditureitem ON category.categoryId = expenditureitem.categoryId
    """
)
data class ExpenditureItemWithCategory (
    val categoryName: String,
    val price: String,
    val content: String,
    val payDate: String,
    val categoryId: Int,
    val expenditureId: Int
)