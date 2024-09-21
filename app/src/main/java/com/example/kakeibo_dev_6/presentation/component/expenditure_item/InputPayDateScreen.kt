package com.example.kakeibo_dev_6.presentation.component.expenditure_item

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kakeibo_dev_6.presentation.component.utility.toDate
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputPayDateScreen(
    payDate: MutableState<String>,
    validationMsg: String
) {

    val yMd = SimpleDateFormat("y年M月d日", Locale.JAPANESE)
    var visible by remember { mutableStateOf(false) }

    Text(
        text = "日付",
        modifier = Modifier.padding(bottom = 4.dp),
        fontWeight = FontWeight.Bold
    )
    Box(
        modifier = Modifier
            .size(280.dp, 50.dp)
            .clip(RoundedCornerShape(4.dp))
            .border(
                BorderStroke(1.dp, Color.LightGray),
                RoundedCornerShape(4.dp)
            )
            .background(Color.White) // 背景色が枠線からはみ出るので背景色のパラメーターはclipとborderの後に設定
            .clickable { visible = !visible },
        contentAlignment = Alignment.CenterStart,
    ) {
        Text(
            text = yMd.format(payDate.value.toDate("yyyy-MM-dd")!!),
            fontSize = 16.sp,
            modifier = Modifier.padding(10.dp)
        )
        Icon(
            imageVector = Icons.Filled.ArrowDropDown,
            contentDescription = "選択アイコン",
            modifier = Modifier.align(Alignment.CenterEnd)
        )
        if (visible) {
            val setState = payDate.value.let { payDate.value.toDate() } ?: Date()
            val state = rememberDatePickerState(setState.time)
            val getDate = state.selectedDateMillis?.let {
                Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
            }
            DatePickerDialog(
                onDismissRequest = { visible = false },
                confirmButton = {
                    Row {
                        TextButton(
                            onClick = { visible = false },
                            content = { Text(text = "キャンセル") })
                        TextButton(
                            onClick = {
                                visible = false
                                payDate.value =
                                    getDate?.let { getDate.toString() + " 12:00:00" }
                                        ?: if (payDate.value == "") LocalDate.now()
                                            .toString() + " 12:00:00" else payDate.value + " 12:00:00"
                            },
                            content = { Text(text = "OK") }
                        )
                    }
                }
            ) {
                DatePicker(
                    state = state,
                    showModeToggle = false,
                    dateValidator = {
                        !Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault())
                            .toLocalDate()
                            .isAfter(LocalDate.now())
                    }
                )
            }
        }
    }
    if (validationMsg != "") {
        Text(
            text = validationMsg,
            color = Color.Red,
            fontSize = 14.sp
        )
    }
}