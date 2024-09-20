package com.example.kakeibo_dev_6.data.entity

import androidx.room.DatabaseView

@DatabaseView(
    viewName = "ExpenditureItemJoinCategory",
    value = """
        SELECT * From expenditureitem INNER JOIN category ON expenditureitem.categoryId = category.id
    """
)
data class ExpenditureItemJoinCategory(
    val id: Int,
    val categoryName: String,
    val price: String,
    val payDate: String,
    val content: String,
    val categoryId: Int
)