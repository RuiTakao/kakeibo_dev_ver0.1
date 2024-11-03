package com.kakeibo.kakeibo_dev_6.presentation.expenditure_item.expenditure_item_list.component.management_content

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun TransitionStatementPageButton(modifier: Modifier) {
    Row(
        modifier = modifier
    ) {
        Text(text = "明細", color = Color(0xFF854A2A))
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = "明細",
            tint = Color(0xFF854A2A)
        )
    }
}