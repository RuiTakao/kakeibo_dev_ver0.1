package com.example.kakeibo_dev_6.domain.model

import androidx.room.DatabaseView

@DatabaseView(
    viewName = "CategorizeExpenditureItem",
    value = """
        SELECT * From category INNER JOIN expenditureitem ON category.id = expenditureitem.categoryId
    """
)
data class CategorizeExpenditureItem(
    val id: Int,
    val categoryName: String,
    val price: String,
    val payDate: String,
    val categoryId: Int,
    val categoryOrder: Int
)