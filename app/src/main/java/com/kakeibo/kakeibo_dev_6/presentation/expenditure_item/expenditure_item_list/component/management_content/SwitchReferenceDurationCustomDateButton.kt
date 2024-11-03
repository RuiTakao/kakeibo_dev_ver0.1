package com.kakeibo.kakeibo_dev_6.presentation.expenditure_item.expenditure_item_list.component.management_content

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Button
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerFormatter
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DateRangePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kakeibo.kakeibo_dev_6.common.enum.DayOrWeekOrMonth
import com.kakeibo.kakeibo_dev_6.common.utility.is_registered_user.isRegisteredUserReferenceCustomDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwitchReferenceDurationCustomDateButton(
    selectedDateProperty: String,
    state: DateRangePickerState,
    onClick: () -> Unit
) {

    // DatePickerの表示非表示の判定
    val visible = remember { mutableStateOf(false) }

    // 選択、未選択の判定
    val selected = selectedDateProperty == DayOrWeekOrMonth.CUSTOM.name

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = { visible.value = !visible.value })
    ) {
        // ボタンのアイコン
        Icon(
            imageVector = Icons.Default.CalendarMonth,
            contentDescription = "カスタム日付",
            // テキストの色、未選択時はグレー
            tint = if (selected) Color(0xFF854A2A) else Color.Gray
        )

        // 選択時のアンダーバー
        Spacer(
            modifier = Modifier
                .padding(top = 2.dp)
                .height(2.dp)
                .width(20.dp)
                .background(if (selected) Color(0xFF854A2A) else Color.Transparent)
        )
    }

    // DatePickerの制御
    if (visible.value) {

        // DatePickerのダイアログ
        DatePickerDialog(
            onDismissRequest = {
                visible.value = false
            },
            confirmButton = {}
        ) {
            Box(contentAlignment = Alignment.BottomEnd) {
                // DatePicker
                DateRangePicker(
                    state = state,
                    // カレンダービューのみ許可、テキストフィールドは表示不可にする
                    showModeToggle = false,
                    // DatePickerに表示される日付のフォーマット、M月d日
                    dateFormatter = DatePickerFormatter(selectedDateSkeleton = "Md"),
                    dateValidator = {

                        // 未来日と二か月前の月の選択を不可にするためのバリデーション
                        // 課金ユーザーは無制限
                        isRegisteredUserReferenceCustomDate(it)
                    }
                )

                // 日付確定ボタン
                Button(
                    onClick = {
                        // DatePickerを閉じる
                        visible.value = false

                        onClick()
                    },
                    modifier = Modifier.padding(16.dp),
                    content = { Text(text = "OK") }
                )
            }
        }
    }
}