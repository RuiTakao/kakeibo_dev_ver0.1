package com.kakeibo.kakeibo_dev_6.presentation.expenditure_item.expenditure_item_list.component.management_content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kakeibo.kakeibo_dev_6.common.utility.priceFormat

/**
 * 表示されている金額の合計金額
 *
 * @param totalPaymentTax Int 金額
 * @param fontSize TextUnit 金額のフォントサイズ
 */
@Composable
fun TotalPaymentText(
    totalPaymentTax: Int,
    fontSize: TextUnit = 24.sp
) {

    Row(
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.Center
    ) {

        Text(text = "使用額", modifier = Modifier.padding(end = 8.dp))

        Text(
            text = "￥${priceFormat(totalPaymentTax.toString())}",
            fontSize = fontSize
        )
    }
}