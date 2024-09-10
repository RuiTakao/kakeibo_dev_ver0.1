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
import androidx.compose.material3.DatePickerFormatter
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
import com.example.kakeibo_dev_6.enum.DateProperty
import com.example.kakeibo_dev_6.viewModel.DisplaySwitchAreaViewModel
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAdjusters
import java.util.Calendar
import java.util.Date

/**
 * 支出項目一覧の表示切り替えエリア
 *
 * @param totalTax Int
 * @param viewModel DisplaySwitchAreaViewModel
 * @param searchArea Boolean
 *
 * @return Unit
 */
@Composable
fun DisplaySwitchArea(
    totalTax: Int,
    viewModel: DisplaySwitchAreaViewModel,
    searchArea: Boolean = false
) {
    Column(
        modifier = Modifier
            .shadow(elevation = 5.dp)
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.onPrimary),
        content = {

            /** 日、週、月、任意の期間毎に支出一覧項目の表示を切り替えるボタンのエリア */
            ChangeDurationDateRow(viewModel = viewModel)

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                content = {

                    /** 過去の支出項目一覧へ表示を切り替えるボタン */
                    PrevButton(viewModel = viewModel)

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        content = {

                            /** 表示期間 */
                            ShowDurationDate(viewModel = viewModel)

                            /** 一覧に表示されている支出項目の合計金額 */
                            Text(
                                text = "使用額 ￥${totalTax}",
                                fontSize = 16.sp,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    )

                    /** 未来の支出項目一覧へ表示を切り替えるボタン */
                    NextButton(viewModel = viewModel)
                }
            )

            /** 明細のみ表示 */
            if (searchArea) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    content = {

                        /** カテゴリー毎に絞り込み */
                        SelectCategoryBox(viewModel = viewModel)
                        Spacer(modifier = Modifier.width(8.dp))

                        /** 入力日付の並び替え */
                        SelectDateSortBox(viewModel = viewModel)
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    )
}

/**
 * 日、週、月、任意の期間毎に支出一覧項目の表示を切り替えるボタンのエリア
 *
 * @param viewModel DisplaySwitchAreaViewModel
 *
 * @return Unit
 */
@Composable
private fun ChangeDurationDateRow(viewModel: DisplaySwitchAreaViewModel) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        content = {

            /** 期間毎に支出一覧項目の表示を切り替えるボタン　日 */
            ChangeDurationDateText(
                text = "日", dateProperty = DateProperty.DAY.name, onClick = {
                    println(viewModel.startDate)
//                    viewModel.startDate = Date()
//                    viewModel.lastDate = Date()
                    viewModel.startDate = viewModel.startDate
                    viewModel.lastDate = viewModel.startDate
                    viewModel.dateProperty = DateProperty.DAY.name
                },
                viewModel = viewModel
            )

            /** 期間毎に支出一覧項目の表示を切り替えるボタン　週 */
            ChangeDurationDateText(
                text = "週", dateProperty = DateProperty.WEEK.name, onClick = {
                    viewModel.startDate = weekStartDate()
                    viewModel.lastDate = weekLastDate()
                    viewModel.dateProperty = DateProperty.WEEK.name
                },
                viewModel = viewModel
            )

            /** 期間毎に支出一覧項目の表示を切り替えるボタン　月 */
            ChangeDurationDateText(
                text = "月", dateProperty = DateProperty.MONTH.name, onClick = {
                    val date = LocalDate.now()
                    val firstDate = date.with(TemporalAdjusters.firstDayOfMonth())
                    val lastDate = date.with(TemporalAdjusters.lastDayOfMonth())
                    viewModel.startDate =
                        Date.from(firstDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
                    viewModel.lastDate =
                        Date.from(lastDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
                    viewModel.dateProperty = DateProperty.MONTH.name
                },
                viewModel = viewModel
            )

            /** 期間毎に支出一覧項目の表示を切り替えるボタン　任意の期間 */
            ChangeDurationDateCustom(viewModel = viewModel)
        }
    )
}

/**
 * 期間毎に支出一覧項目の表示を切り替えるボタン
 *
 * @param text String 表示するボタンテキスト
 * @param dateProperty DateProperty ボタンの種類
 * @param onClick () -> Unit
 * @param viewModel DisplaySwitchAreaViewModel
 *
 * @return Unit
 */
@Composable
private fun ChangeDurationDateText(
    text: String,
    dateProperty: String,
    onClick: () -> Unit,
    viewModel: DisplaySwitchAreaViewModel
) {
    val selected = viewModel.dateProperty == dateProperty

    TextButton(
        onClick = onClick,
        content = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                content = {
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
            )
        },
        enabled = if (selected) false else true
    )
}

/**
 * 期間毎に支出一覧項目の表示を切り替えるボタン　任意の期間
 *
 * @param viewModel DisplaySwitchAreaViewModel
 *
 * @return Unit
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChangeDurationDateCustom(viewModel: DisplaySwitchAreaViewModel) {

    val selected = viewModel.dateProperty == DateProperty.CUSTOM.name

    val visible = remember { mutableStateOf(false) }

    IconButton(
        onClick = { visible.value = !visible.value },
        content = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                content = {
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
            )
        }
    )

    if (visible.value) {

        val state = rememberDateRangePickerState(viewModel.startDate.time, viewModel.lastDate.time)
        val getStartDate = state.selectedStartDateMillis?.let { Date(it) } ?: Date()
        val getLastDate = state.selectedEndDateMillis?.let { Date(it) } ?: Date()

        DatePickerDialog(
            onDismissRequest = { visible.value = false },
            confirmButton = {},
            content = {
                Box(
                    contentAlignment = Alignment.BottomEnd,
                    content = {
                        DateRangePicker(
                            state = state,
                            showModeToggle = false,
                            dateFormatter = DatePickerFormatter(selectedDateSkeleton = "Md"),
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
                                viewModel.dateProperty = DateProperty.CUSTOM.name
                                viewModel.startDate = getStartDate
                                viewModel.lastDate = getLastDate
                            },
                            modifier = Modifier.padding(16.dp),
                            content = { Text(text = "OK") }
                        )
                    }
                )
            }
        )
    }
}

/**
 * 表示期間
 *
 * @param viewModel: DisplaySwitchAreaViewModel
 *
 * @return Unit
 */
@Composable
private fun ShowDurationDate(viewModel: DisplaySwitchAreaViewModel) {

    // 日付フォーマット
    val Md = SimpleDateFormat("M月d日")
    val M = SimpleDateFormat("M月")

    // 表示日付のテキスト
    var text = ""
    when (viewModel.dateProperty) {
        DateProperty.DAY.name -> text = Md.format(viewModel.startDate)
        DateProperty.WEEK.name -> text =
            "${Md.format(viewModel.startDate)} 〜 ${Md.format(viewModel.lastDate)}"

        DateProperty.MONTH.name -> text = M.format(viewModel.startDate)
        DateProperty.CUSTOM.name -> text =
            "${Md.format(viewModel.startDate)} 〜 ${Md.format(viewModel.lastDate)}"
    }

    Text(text = text, fontSize = 24.sp)
}

/**
 * 過去の支出項目一覧へ表示を切り替えるボタン
 *
 * @param viewModel: DisplaySwitchAreaViewModel
 *
 * @return Unit
 */
@Composable
private fun PrevButton(viewModel: DisplaySwitchAreaViewModel) {
    IconButton(
        onClick = { onClickPrevButton(viewModel = viewModel) },
        content = { Icon(imageVector = Icons.Default.KeyboardArrowLeft, contentDescription = null) }
    )
}

/**
 * 過去の支出項目一覧へ表示を切り替え
 *
 * @param viewModel: DisplaySwitchAreaViewModel
 *
 * @return Unit
 */
private fun onClickPrevButton(viewModel: DisplaySwitchAreaViewModel) {

    // 日付計算用のカレンダーインスタンス作成
    val getDate = Calendar.getInstance()

    when (viewModel.dateProperty) {

        // 日
        DateProperty.DAY.name -> {

            // 日が選択されている場合は開始日を基準に計算
            getDate.time = viewModel.startDate

            // 対象の日付に一日減算
            getDate.add(Calendar.DATE, -1)

            // 計算結果をViewModelに保存
            viewModel.lastDate = getDate.time
            viewModel.startDate = getDate.time
        }

        // 週
        DateProperty.WEEK.name -> {

            // 週が選択されている場合は開始日を基準に計算
            getDate.time = viewModel.startDate

            // 最終日に一日減算
            getDate.add(Calendar.DATE, -1)
            viewModel.lastDate = getDate.time

            // 開始日に六日減算
            getDate.add(Calendar.DATE, -6)
            viewModel.startDate = getDate.time
        }

        // 月
        DateProperty.MONTH.name -> {

            // 月が選択されている場合は開始日を基準に計算
            getDate.time = viewModel.startDate

            // 一月減算
            getDate.add(Calendar.MONTH, -1)

            // 対象月の開始日と最終日を求める為、Calendar型からLocalDate型に変換する
            val changeDateToLocalDate =
                getDate.time.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()

            // 対象月の開始日を取得
            val firstDate = changeDateToLocalDate.with(TemporalAdjusters.firstDayOfMonth())

            // 対象月の終了日を取得
            val lastDate = changeDateToLocalDate.with(TemporalAdjusters.lastDayOfMonth())

            // 対象月の開始日をDate型に変換してViewModelに保存
            viewModel.startDate =
                Date.from(firstDate.atStartOfDay(ZoneId.systemDefault()).toInstant())

            // 対象月の終了日をDate型に変換してViewModelに保存
            viewModel.lastDate =
                Date.from(lastDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
        }

        // 任意の期間
        DateProperty.CUSTOM.name -> {

            // 比較用の開始日と終了日をLocalDate型に変換し取得
            val diffStartDate =
                viewModel.startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
            val diffLastDate =
                viewModel.lastDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()

            // 終了日と開始日の差分を調べる
            val diff = ChronoUnit.DAYS.between(diffStartDate, diffLastDate)

            // 対象の日付を取得
            val getStartDate = Calendar.getInstance()
            getStartDate.time = viewModel.startDate
            val getLastDate = Calendar.getInstance()
            getLastDate.time = viewModel.lastDate

            // 対象の日付の差分を減算
            getStartDate.add(Calendar.DATE, diff.toInt() * -1)
            getLastDate.add(Calendar.DATE, diff.toInt() * -1)

            // ViewModelに保存
            viewModel.startDate = getStartDate.time
            viewModel.lastDate = getLastDate.time
        }
    }
}

/**
 * 未来の支出項目一覧へ表示を切り替えるボタン
 *
 * @param viewModel: DisplaySwitchAreaViewModel
 *
 * @return Unit
 */
@Composable
private fun NextButton(viewModel: DisplaySwitchAreaViewModel) {
    val lastDate = viewModel.lastDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
    IconButton(
        onClick = { onClickNextButton(viewModel = viewModel) },
        enabled = if (lastDate.isEqual(LocalDate.now()) || lastDate.isAfter(LocalDate.now())) false else true,
        content = {
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = null
            )
        }
    )
}

/**
 * 未来の支出項目一覧へ表示を切り替え
 *
 * @param viewModel: DisplaySwitchAreaViewModel
 *
 * @return Unit
 */
private fun onClickNextButton(viewModel: DisplaySwitchAreaViewModel) {

    // 日付計算用のカレンダーインスタンス作成
    val getDate = Calendar.getInstance()

    when (viewModel.dateProperty) {

        // 日
        DateProperty.DAY.name -> {

            // 日が選択されている場合は最終日を基準に計算
            getDate.time = viewModel.lastDate

            // 対象の日付に一日加算
            getDate.add(Calendar.DATE, 1)

            // 計算結果をViewModelに保存
            viewModel.lastDate = getDate.time
            viewModel.startDate = getDate.time
        }

        // 週
        DateProperty.WEEK.name -> {

            // 週が選択されている場合は最終日を基準に計算
            getDate.time = viewModel.lastDate

            // 最終日に一日加算
            getDate.add(Calendar.DATE, 1)
            viewModel.startDate = getDate.time

            // 開始日に六日加算
            getDate.add(Calendar.DATE, 6)
            viewModel.lastDate = getDate.time
        }

        // 月
        DateProperty.MONTH.name -> {

            // 月が選択されている場合は開始日を基準に計算
            getDate.time = viewModel.startDate

            // 一月加算
            getDate.add(Calendar.MONTH, 1)

            // 対象月の開始日と最終日を求める為、Calendar型からLocalDate型に変換する
            val changeDateToLocalDate =
                getDate.time.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()

            // 対象月の開始日を取得
            val firstDate = changeDateToLocalDate.with(TemporalAdjusters.firstDayOfMonth())

            // 対象月の最終日を取得
            val lastDate = changeDateToLocalDate.with(TemporalAdjusters.lastDayOfMonth())

            // 対象月の開始日をDate型に変換してViewModelに保存
            viewModel.startDate =
                Date.from(firstDate.atStartOfDay(ZoneId.systemDefault()).toInstant())

            // 対象月の最終日をDate型に変換してViewModelに保存
            viewModel.lastDate =
                Date.from(lastDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
        }

        // 任意の期間
        DateProperty.CUSTOM.name -> {

            // 比較用の開始日と終了日をLocalDate型に変換し取得
            val diffStartDate =
                viewModel.startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
            val diffLastDate =
                viewModel.lastDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()

            // 終了日と開始日の差分を調べる
            val diff = ChronoUnit.DAYS.between(diffStartDate, diffLastDate)

            // 対象の日付を取得
            val getStartDate = Calendar.getInstance()
            getStartDate.time = viewModel.startDate
            val getLastDate = Calendar.getInstance()
            getLastDate.time = viewModel.lastDate

            // 対象の日付の差分を加算
            getStartDate.add(Calendar.DATE, diff.toInt())
            getLastDate.add(Calendar.DATE, diff.toInt())

            // ViewModelに保存
            viewModel.startDate = getStartDate.time
            viewModel.lastDate = getLastDate.time
        }
    }
}

/**
 * カテゴリー毎に絞り込み
 *
 * @param viewModel: DisplaySwitchAreaViewModel
 *
 * @return Unit
 */
@Composable
private fun SelectCategoryBox(viewModel: DisplaySwitchAreaViewModel) {

    val selectCategory = remember { mutableStateOf(viewModel.selectCategory) }
    val selectCategoryName = remember { mutableStateOf("すべて") }
    val expanded = remember { mutableStateOf(false) }
    val categories by viewModel.category.collectAsState(initial = emptyList())
    categories.forEach {
        if (it.id == selectCategory.value) {
            selectCategoryName.value = it.categoryName
        }
    }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable { expanded.value = !expanded.value },
        content = {
            Text(text = selectCategoryName.value, modifier = Modifier.padding(start = 10.dp))
            Icon(
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = "選択アイコン",
            )
            DropdownMenu(
                expanded = expanded.value,
                onDismissRequest = { expanded.value = false },
                content = {
                    DropdownMenuItem(
                        text = { Text(text = "すべて") },
                        onClick = {
                            expanded.value = false
                            selectCategory.value = 0
                            selectCategoryName.value = "すべて"
                            viewModel.selectCategory = 0
                        }
                    )
                    categories.forEach { selectOption ->
                        DropdownMenuItem(
                            text = { Text(text = selectOption.categoryName) },
                            onClick = {
                                expanded.value = false
                                selectCategory.value = selectOption.id
                                selectCategoryName.value = selectOption.categoryName
                                viewModel.selectCategory = selectOption.id
                            }
                        )
                    }
                }
            )
        }
    )
}

/**
 * 入力日付の並び替え
 *
 * @param viewModel: DisplaySwitchAreaViewModel
 *
 * @return Unit
 */
@Composable
private fun SelectDateSortBox(viewModel: DisplaySwitchAreaViewModel) {

    val sort = remember { mutableStateOf(viewModel.sort) }
    val expanded = remember { mutableStateOf(false) }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable { expanded.value = !expanded.value },
        content = {
            Text(
                text = "日付${if (sort.value) "昇順" else "降順"}",
                modifier = Modifier.padding(start = 10.dp)
            )
            Icon(
                imageVector = Icons.Filled.ArrowDropDown, contentDescription = "選択アイコン"
            )
            DropdownMenu(
                expanded = expanded.value,
                onDismissRequest = { expanded.value = false },
                content = {
                    DropdownMenuItem(
                        text = { Text(text = "日付降順") },
                        onClick = {
                            expanded.value = false
                            sort.value = false
                            viewModel.sort = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(text = "日付昇順") },
                        onClick = {
                            expanded.value = false
                            sort.value = true
                            viewModel.sort = true
                        }
                    )
                }
            )
        }
    )
}