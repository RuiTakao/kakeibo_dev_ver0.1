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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kakeibo_dev_6.enum.DateProperty
import com.example.kakeibo_dev_6.enum.SwitchDate
import com.example.kakeibo_dev_6.viewModel.DisplaySwitchAreaViewModel
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
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
                text = "日",
                dateProperty = DateProperty.DAY.name,
                onClick = {
                    viewModel.dateProperty = DateProperty.DAY.name
                },
                viewModel = viewModel
            )

            /** 期間毎に支出一覧項目の表示を切り替えるボタン　週 */
            ChangeDurationDateText(
                text = "週",
                dateProperty = DateProperty.WEEK.name,
                onClick = {
                    viewModel.dateProperty = DateProperty.WEEK.name
                },
                viewModel = viewModel
            )

            /** 期間毎に支出一覧項目の表示を切り替えるボタン　月 */
            ChangeDurationDateText(
                text = "月",
                dateProperty = DateProperty.MONTH.name,
                onClick = {
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

        /**  */
        val state = rememberDateRangePickerState(
            viewModel.customOfStartDate.time,
            viewModel.customOfLastDate.time
        )
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
                                viewModel.customOfStartDate = getStartDate
                                viewModel.customOfLastDate = getLastDate
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
        DateProperty.DAY.name -> text =
            Md.format(viewModel.standardOfStartDate)

        /** 表示期間、週 */
        DateProperty.WEEK.name -> {
            val getDate = Calendar.getInstance()
            getDate.time = viewModel.standardOfStartDate
            getDate.add(Calendar.DATE, getDate.get(Calendar.DAY_OF_WEEK) * -1 + 1)
            val startDate = getDate.time
            getDate.add(Calendar.DATE, 6)
            val lastDate = getDate.time
            text =
                "${
                    Md.format(startDate)
                } 〜 ${
                    Md.format(lastDate)
                }"
        }

        DateProperty.MONTH.name -> text = M.format(viewModel.standardOfStartDate)
        DateProperty.CUSTOM.name -> text =
            "${
                Md.format(viewModel.customOfStartDate)
            } 〜 ${
                Md.format(viewModel.customOfLastDate)
            }"
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
    var enabled = true
    if (viewModel.dateProperty == DateProperty.CUSTOM.name) {
        enabled = false
    }
    IconButton(
        onClick = {
            onClickSwitchDateButton(
                viewModel = viewModel,
                switchAction = SwitchDate.PREV
            )
        },
        enabled = enabled,
        content = { Icon(imageVector = Icons.Default.KeyboardArrowLeft, contentDescription = null) }
    )
}

/**
 * Nextボタン
 * 用途： 未来の支出項目を表示
 *
 * @param viewModel: DisplaySwitchAreaViewModel
 *
 * @return Unit
 */
@Composable
private fun NextButton(viewModel: DisplaySwitchAreaViewModel) {
    val checkDate =
        viewModel.standardOfStartDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
    var enabled = true
    if (
        viewModel.dateProperty == DateProperty.CUSTOM.name ||
        checkDate.isEqual(LocalDate.now())
    ) {
        enabled = false
    } else {
        when (viewModel.dateProperty) {

            DateProperty.WEEK.name -> {
                val getDate = Calendar.getInstance()
                getDate.time = viewModel.standardOfStartDate
                getDate.add(Calendar.DATE, getDate.get(Calendar.DAY_OF_WEEK) * -1 + 7)
                if (getDate.time.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                        .isAfter(LocalDate.now())
                ) {
                    enabled = false
                }
            }

            DateProperty.MONTH.name -> {
                if (
                    LocalDate.now().with(TemporalAdjusters.firstDayOfMonth())
                        .isEqual(checkDate.with(TemporalAdjusters.firstDayOfMonth()))
                ) {
                    enabled = false
                }
            }
        }
    }
    IconButton(
        onClick = {
            onClickSwitchDateButton(
                viewModel = viewModel,
                switchAction = SwitchDate.NEXT
            )
        },
        enabled = enabled,
        content = {
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = null
            )
        }
    )
}

/**
 * 日付切り替え
 *
 * @param viewModel DisplaySwitchAreaViewModel
 * @param switchAction SwitchDate prevかnextかの判定
 *
 * @return Unit
 */
private fun onClickSwitchDateButton(
    viewModel: DisplaySwitchAreaViewModel,
    switchAction: SwitchDate
) {

    /** 日付計算用のカレンダーインスタンス作成 */
    val getDate = Calendar.getInstance()

    when (viewModel.dateProperty) {

        /** dateProperty 日 */
        DateProperty.DAY.name -> {

            /**
             *  datePropertyが日の場合は開始日を基準に計算
             *  prevの場合は１日減算して開始日と終了日にセット
             *  nextの場合は１日加算して開始日と終了日にセット
             */
            getDate.time = viewModel.standardOfStartDate
            val amount =
                when (switchAction) {
                    SwitchDate.PREV -> -1
                    SwitchDate.NEXT -> 1
                }
            getDate.add(Calendar.DATE, amount)
            viewModel.standardOfStartDate = getDate.time
        }

        /** dateProperty 週 */
        DateProperty.WEEK.name -> {

            /** prevとnextで開始日と終了日の更新手順が違うので手順自体を分岐 */
            when (switchAction) {
                SwitchDate.PREV -> {
                    /**
                     * prevの場合は開始日を基準に計算
                     * 現在の開始日から１日減算した日付を終了日に設定
                     * 設定した終了日から６日減算した日付を開始日に設定
                     */
                    getDate.time = viewModel.standardOfStartDate
                    getDate.add(Calendar.DATE, -7)
                    viewModel.standardOfStartDate = getDate.time
                }

                SwitchDate.NEXT -> {
                    /**
                     * nextの場合は終了日を基準に計算
                     * 現在の終了日から１日加算した日付を開始日に設定
                     * 設定した開始日から６日加算した日付を終了日に設定
                     */
                    getDate.time = viewModel.standardOfStartDate
                    getDate.add(Calendar.DATE, 7)
                    viewModel.standardOfStartDate = getDate.time
                }
            }
        }

        /** dateProperty 月 */
        DateProperty.MONTH.name -> {

            /**
             * datePropertyが月の場合は開始日を基準に計算
             * prevの場合はgetDateから１月減算
             * nextの場合はgetDateから１月加算
             */
            getDate.time = viewModel.standardOfStartDate
            val amount =
                when (switchAction) {
                    SwitchDate.PREV -> -1
                    SwitchDate.NEXT -> 1
                }
            getDate.add(Calendar.MONTH, amount)
            viewModel.standardOfStartDate = getDate.time
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