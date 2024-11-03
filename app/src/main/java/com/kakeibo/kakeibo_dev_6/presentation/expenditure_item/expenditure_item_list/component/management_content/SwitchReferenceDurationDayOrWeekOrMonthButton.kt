package com.kakeibo.kakeibo_dev_6.presentation.expenditure_item.expenditure_item_list.component.management_content

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kakeibo.kakeibo_dev_6.common.enum.DayOrWeekOrMonth

/**
 * 日、週、月毎に表示期間の切り替えボタン
 *
 * @param dayOrWeekOrMonthProperty String ボタンの使われ方
 * @param selectedDayOrWeekOrMonth String 現在の期間
 * @param onClick () -> Unit クリック動作
 *
 * @return Unit
 */
@Composable
fun SwitchReferenceDurationDayOrWeekOrMonthButton(
    dayOrWeekOrMonthProperty: String,
    selectedDayOrWeekOrMonth: String,
    onClick: () -> Unit
) {
    // 選択、未選択の判定
    val enabled = selectedDayOrWeekOrMonth != dayOrWeekOrMonthProperty

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(
            enabled = enabled,
            onClick = onClick
        )
    ) {

        // ボタンのテキスト
        Text(
            text = when (dayOrWeekOrMonthProperty) {
                DayOrWeekOrMonth.DAY.name -> "日"
                DayOrWeekOrMonth.WEEK.name -> "週"
                DayOrWeekOrMonth.MONTH.name -> "月"
                else -> ""
            },
            fontSize = 16.sp,
            // テキストの色、未選択時はグレー
            color = if (!enabled) Color(0xFF854A2A) else Color.Gray
        )

        // 選択時のアンダーバー
        Spacer(
            modifier = Modifier
                .padding(top = 2.dp)
                .height(2.dp)
                .width(20.dp)
                // アンダーバーの色、未選択時は透過
                .background(if (!enabled) Color(0xFF854A2A) else Color.Transparent)
        )
    }
}