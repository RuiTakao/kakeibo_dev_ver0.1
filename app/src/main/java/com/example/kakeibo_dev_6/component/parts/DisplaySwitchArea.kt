package com.example.kakeibo_dev_6.component.parts

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kakeibo_dev_6.component.utility.weekLastDate
import com.example.kakeibo_dev_6.component.utility.weekStartDate
import com.example.kakeibo_dev_6.viewModel.DisplaySwitchAreaViewModel
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAdjusters
import java.util.Calendar
import java.util.Date

@Composable
fun DisplaySwitchArea(
    totalTax: Int, viewModel: DisplaySwitchAreaViewModel, searchArea: Boolean = false
) {
    Column(
        modifier = Modifier
            .shadow(elevation = 5.dp)
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.onPrimary)
    ) {
        ChangeDurationDateRow(viewModel = viewModel)
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            PrevButton(viewModel = viewModel)
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                ShowDurationDate(viewModel = viewModel)
                Text(
                    text = "使用額 ￥${totalTax}",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            NextButton(viewModel = viewModel)
        }

        // 検索エリア
        if (searchArea) {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
            ) {
                SelectCategoryBox(viewModel = viewModel)
                Spacer(modifier = Modifier.width(8.dp))
                SelectDateSortBox(viewModel = viewModel)
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

// 表示期間切り替えアイコンコンテンツ
@Composable
private fun ChangeDurationDateRow(viewModel: DisplaySwitchAreaViewModel) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    ) {
        ChangeDurationDateText(
            text = "日", dateProperty = "day", onClick = {
                viewModel.startDate = Date()
                viewModel.lastDate = Date()
                viewModel.dateProperty = "day"
            }, viewModel = viewModel
        )
        ChangeDurationDateText(
            text = "週", dateProperty = "week", onClick = {
                viewModel.startDate = weekStartDate()
                viewModel.lastDate = weekLastDate()
                viewModel.dateProperty = "week"
            }, viewModel = viewModel
        )
        ChangeDurationDateText(
            text = "月", dateProperty = "month", onClick = {
                val date = LocalDate.now()
                val firstDate = date.with(TemporalAdjusters.firstDayOfMonth())
                val lastDate = date.with(TemporalAdjusters.lastDayOfMonth())
                viewModel.startDate =
                    Date.from(firstDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
                viewModel.lastDate =
                    Date.from(lastDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
                viewModel.dateProperty = "month"
            }, viewModel = viewModel
        )
        ChangeDurationDateCustom(viewModel = viewModel)
    }
}

// 表示期間切り替えアイコン
@Composable
private fun ChangeDurationDateText(
    text: String, dateProperty: String, onClick: () -> Unit, viewModel: DisplaySwitchAreaViewModel
) {
    val selected = viewModel.dateProperty == dateProperty

    TextButton(
        onClick = onClick, content = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = text,
                    fontSize = 16.sp,
                    color = if (selected) Color(0xFF854A2A) else Color.Gray
                )
                Spacer(modifier = Modifier.height(2.dp))
                Spacer(
                    modifier = Modifier
                        .height(2.dp)
                        .width(20.dp)
                        .background(if (selected) Color(0xFF854A2A) else Color.Transparent)
                )
            }
        }, enabled = if (selected) false else true
    )
}

// 表示期間切り替えカスタム
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChangeDurationDateCustom(viewModel: DisplaySwitchAreaViewModel) {

    val selected = viewModel.dateProperty == "custom"

    val visible = remember { mutableStateOf(false) }

    IconButton(onClick = { visible.value = !visible.value }, content = {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.CalendarMonth,
                contentDescription = null,
                tint = if (selected) Color(0xFF854A2A) else Color.Gray
            )
            Spacer(modifier = Modifier.height(2.dp))
            Spacer(
                modifier = Modifier
                    .height(2.dp)
                    .width(20.dp)
                    .background(if (selected) Color(0xFF854A2A) else Color.Transparent)
            )
        }
    })

    if (visible.value) {

        val state = rememberDateRangePickerState(viewModel.startDate.time, viewModel.lastDate.time)
        val coroutineScope = rememberCoroutineScope()
        val getStartDate = state.selectedStartDateMillis?.let { Date(it) } ?: Date()
        val getLastDate = state.selectedEndDateMillis?.let { Date(it) } ?: Date()

        DatePickerDialog(onDismissRequest = { visible.value = false },
            confirmButton = {
//                TextButton(onClick = { visible.value = false }, content = { Text(text = "OK") })
            },
            content = {
                Box(contentAlignment = Alignment.BottomEnd) {
                    DateRangePicker(
                        state = state,
                        dateValidator = {
                            if (Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault())
                                    .toLocalDate()
                                    .isAfter(LocalDate.now())
                            ) false else true
                        }
                    )
                    Button(
                        onClick = {
                            visible.value = false
                            viewModel.dateProperty = "custom"
                            viewModel.startDate = getStartDate
                            viewModel.lastDate = getLastDate
                        },
                        modifier = Modifier.padding(16.dp),
                        content = {
                            Box(
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "OK", color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        }
                    )

                }
            }
        )
    }
}

// 表示する期間
@Composable
private fun ShowDurationDate(viewModel: DisplaySwitchAreaViewModel) {
    val Md = SimpleDateFormat("M月d日")
    val M = SimpleDateFormat("M月")
    var text = ""
    when (viewModel.dateProperty) {
        "day" -> text = Md.format(viewModel.startDate)
        "week" -> text = "${Md.format(viewModel.startDate)} 〜 ${Md.format(viewModel.lastDate)}"
        "month" -> text = M.format(viewModel.startDate)
        "custom" -> text = "${Md.format(viewModel.startDate)} 〜 ${Md.format(viewModel.lastDate)}"
    }
    Text(text = text, fontSize = 24.sp)
}

// prevボタン
@Composable
private fun PrevButton(viewModel: DisplaySwitchAreaViewModel) {
    IconButton(onClick = {
        when (viewModel.dateProperty) {
            "day" -> {
                val getDate = Calendar.getInstance()
                getDate.time = viewModel.startDate
                getDate.add(Calendar.DATE, -1)
                viewModel.lastDate = getDate.time
                viewModel.startDate = getDate.time
            }

            "week" -> {
                val getDate = Calendar.getInstance()
                getDate.time = viewModel.startDate
                getDate.add(Calendar.DATE, -1)
                viewModel.lastDate = getDate.time
                getDate.add(Calendar.DATE, -6)
                viewModel.startDate = getDate.time
            }

            "month" -> {
                val getDate = Calendar.getInstance()
                getDate.time = viewModel.startDate
                getDate.add(Calendar.MONTH, -1)

                val changeDateToLocalDate =
                    getDate.time.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                val firstDate = changeDateToLocalDate.with(TemporalAdjusters.firstDayOfMonth())
                val lastDate = changeDateToLocalDate.with(TemporalAdjusters.lastDayOfMonth())
                viewModel.startDate =
                    Date.from(firstDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
                viewModel.lastDate =
                    Date.from(lastDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
            }

            "custom" -> {

                // 開始日と終了日をLocalDateに変換
                val diffStartDate =
                    viewModel.startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                val diffLastDate =
                    viewModel.lastDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                // 終了日と開始日の差分を調べる

                val diff = ChronoUnit.DAYS.between(diffStartDate, diffLastDate)
                Log.d("diff", diff.toString())
                // 開始日、終了日それぞれに差分を引いて再代入
                val getStartDate = Calendar.getInstance()
                val getLastDate = Calendar.getInstance()
                getStartDate.time = viewModel.startDate
                getLastDate.time = viewModel.lastDate
                getStartDate.add(Calendar.DATE, diff.toInt() * -1)
                getLastDate.add(Calendar.DATE, diff.toInt() * -1)
                viewModel.startDate = getStartDate.time
                viewModel.lastDate = getLastDate.time
            }
        }
    }) {
        Icon(
            imageVector = Icons.Default.KeyboardArrowLeft, contentDescription = null
        )
    }
}

/**
 * nextボタン
 *
 * @param viewModel: DisplaySwitchAreaViewModel
 *
 * @return Unit
 */
@Composable
private fun NextButton(viewModel: DisplaySwitchAreaViewModel) {
    val lastDate = viewModel.lastDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
    IconButton(
        onClick = {
            when (viewModel.dateProperty) {
                "day" -> {
                    val getDate = Calendar.getInstance()
                    getDate.time = viewModel.lastDate
                    getDate.add(Calendar.DATE, 1)
                    viewModel.lastDate = getDate.time
                    viewModel.startDate = getDate.time
                }

                "week" -> {
                    val getDate = Calendar.getInstance()
                    getDate.time = viewModel.lastDate
                    getDate.add(Calendar.DATE, 1)
                    viewModel.startDate = getDate.time
                    getDate.add(Calendar.DATE, 6)
                    viewModel.lastDate = getDate.time
                }

                "month" -> {
                    val getDate = Calendar.getInstance()
                    getDate.time = viewModel.startDate
                    getDate.add(Calendar.MONTH, 1)

                    val changeDateToLocalDate =
                        getDate.time.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                    val firstDate = changeDateToLocalDate.with(TemporalAdjusters.firstDayOfMonth())
                    val lastDate = changeDateToLocalDate.with(TemporalAdjusters.lastDayOfMonth())
                    viewModel.startDate =
                        Date.from(firstDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
                    viewModel.lastDate =
                        Date.from(lastDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
                }

                "custom" -> {

                    // 開始日と終了日をLocalDateに変換
                    val diffStartDate =
                        viewModel.startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                    val diffLastDate =
                        viewModel.lastDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                    // 終了日と開始日の差分を調べる

                    val diff = ChronoUnit.DAYS.between(diffStartDate, diffLastDate)
                    Log.d("diff", diff.toString())
                    // 開始日、終了日それぞれに差分を引いて再代入
                    val getStartDate = Calendar.getInstance()
                    val getLastDate = Calendar.getInstance()
                    getStartDate.time = viewModel.startDate
                    getLastDate.time = viewModel.lastDate
                    getStartDate.add(Calendar.DATE, diff.toInt())
                    getLastDate.add(Calendar.DATE, diff.toInt())
                    viewModel.startDate = getStartDate.time
                    viewModel.lastDate = getLastDate.time
                }
            }
        },
        enabled = if (lastDate.isEqual(LocalDate.now()) || lastDate.isAfter(LocalDate.now())) false else true,
    ) {
        Icon(
            imageVector = Icons.Default.KeyboardArrowRight, contentDescription = null
        )
    }
}

// カテゴリのセレクトボックス
@Composable
private fun SelectCategoryBox(viewModel: DisplaySwitchAreaViewModel) {

    val selectCategory = remember { mutableStateOf(viewModel.selectCategory) }
    val selectCategoryName = remember { mutableStateOf("すべて") }
    val expanded = remember { mutableStateOf(false) }
    val categories by viewModel.category.collectAsState(initial = emptyList())
    categories.forEach{
        if (it.id == selectCategory.value) {
            selectCategoryName.value = it.categoryName
        }
    }

    Row(horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable { expanded.value = !expanded.value }) {
        Text(text = selectCategoryName.value, modifier = Modifier.padding(start = 10.dp))
        Icon(
            imageVector = Icons.Filled.ArrowDropDown,
            contentDescription = "選択アイコン",
        )
        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false },
        ) {
            DropdownMenuItem(text = { Text(text = "すべて") }, onClick = {
                expanded.value = false
                selectCategory.value = 0
                selectCategoryName.value = "すべて"
                viewModel.selectCategory = 0
            })
            categories.forEach { selectOption ->
                DropdownMenuItem(text = { Text(text = selectOption.categoryName) }, onClick = {
                    expanded.value = false
                    selectCategory.value = selectOption.id
                    selectCategoryName.value = selectOption.categoryName
                    viewModel.selectCategory = selectOption.id
                })
            }
        }
    }
}

// 表示順のセレクトボックス
@Composable
private fun SelectDateSortBox(viewModel: DisplaySwitchAreaViewModel) {

    val sort = remember { mutableStateOf(viewModel.sort) }
    val expanded = remember { mutableStateOf(false) }

    Row(horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable { expanded.value = !expanded.value }) {
        Text(
            text = "日付${if (sort.value) "昇順" else "降順"}",
            modifier = Modifier.padding(start = 10.dp)
        )
        Icon(
            imageVector = Icons.Filled.ArrowDropDown, contentDescription = "選択アイコン"
        )
        DropdownMenu(expanded = expanded.value, onDismissRequest = { expanded.value = false }) {
            DropdownMenuItem(text = { Text(text = "日付降順") }, onClick = {
                expanded.value = false
                sort.value = false
                viewModel.sort = false
            })
            DropdownMenuItem(text = { Text(text = "日付昇順") }, onClick = {
                expanded.value = false
                sort.value = true
                viewModel.sort = true
            })
        }
    }
}