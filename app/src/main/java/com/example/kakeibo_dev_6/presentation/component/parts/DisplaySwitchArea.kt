package com.example.kakeibo_dev_6.presentation.component.parts

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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kakeibo_dev_6.common.enum.DateProperty
import com.example.kakeibo_dev_6.common.enum.SwitchDate
import com.example.kakeibo_dev_6.presentation.viewModel.ExpenditureListViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
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
    viewModel: ExpenditureListViewModel,
    searchArea: Boolean = false
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(color = Color.White),
        content = {

            /*
            日、週、月、カスタムボタンエリア
            一行目のレイアウト
             */
            ChangeDurationDateRow(viewModel = viewModel)

            /* 二行目、三行目のレイアウト */
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth() // 幅いっぱいに広げたいためfillMaxWidthをmodifierにセットしておく
                    .padding(vertical = 16.dp),
                content = {

                    /* 前へボタン（過去に移動） */
                    PrevButton(viewModel = viewModel)

                    /* 真ん中のレイアウト */
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        content = {

                            /*
                            日付の表示（表示期間）
                            二行目のレイアウト
                             */
                            ShowDurationDate(viewModel = viewModel)

                            /*
                            合計金額（表示額合計）
                            三行目のレイアウト
                             */
                            Text(
                                text = "使用額 ￥${totalTax}",
                                fontSize = 16.sp,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    )

                    /* 次へボタン（未来へ移動） */
                    NextButton(viewModel = viewModel)
                }
            )

            /*
            明細画面のみ表示
            四行目のレイアウト
             */
            if (searchArea) {

                /* 三行目との余白 */
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(), // 幅いっぱいに広げたいためfillMaxWidthをmodifierにセットしておく
                    content = {

                        /* 絞り込みドロップダウン（カテゴリー毎） */
                        SelectCategoryBox(viewModel = viewModel)

                        /* 左右の余白 */
                        Spacer(modifier = Modifier.width(8.dp))

                        /* 並び替えドロップダウン（登録日付順） */
                        SelectDateSortBox(viewModel = viewModel)
                    }
                )

                /* 下の余白 */
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    )
}

/**
 * 日、週、月、カスタムボタンのエリア
 *
 * @param viewModel DisplaySwitchAreaViewModel
 *
 * @return Unit
 */
@Composable
private fun ChangeDurationDateRow(viewModel: ExpenditureListViewModel) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth() // 幅いっぱいに広げたいためfillMaxWidthをmodifierにセットしておく
            .padding(top = 8.dp),
        content = {

            /* 日ボタン */
            ChangeDurationDateText(
                text = "日",
                dateProperty = DateProperty.DAY.name,
                onClick = {
                    viewModel.dateProperty = DateProperty.DAY.name

                    /*
                    日付の移動後、選択期間を変更した場合、基準日が本日以降の日付になってしまう事がある為
                    日、週で切り替えた場合は基準日が本日以降になっていないか確認し、修正する
                     */
                    viewModel.initStandardDate()
                },
                viewModel = viewModel
            )

            /* 週ボタン */
            ChangeDurationDateText(
                text = "週",
                dateProperty = DateProperty.WEEK.name,
                onClick = {
                    viewModel.dateProperty = DateProperty.WEEK.name

                    /*
                    日付の移動後、選択期間を変更した場合、基準日が本日以降の日付になってしまう事がある為
                    日、週で切り替えた場合は基準日が本日以降になっていないか確認し、修正する
                     */
                    viewModel.initStandardDate()
                },
                viewModel = viewModel
            )

            /* 月ボタン */
            ChangeDurationDateText(
                text = "月",
                dateProperty = DateProperty.MONTH.name,
                onClick = { viewModel.dateProperty = DateProperty.MONTH.name },
                viewModel = viewModel
            )

            /* カスタムボタン */
            ChangeDurationDateCustom(viewModel = viewModel)
        }
    )
}

/**
 * 日、週、月のボタンフォーマット
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
    viewModel: ExpenditureListViewModel
) {
    // 選択、未選択の判定
    val selected = viewModel.dateProperty == dateProperty

    TextButton(
        onClick = onClick,
        content = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                content = {

                    /* ボタンのテキスト */
                    Text(
                        text = text,
                        fontSize = 16.sp,
                        // テキストの色、未選択時はグレー
                        color = if (selected) Color(0xFF854A2A) else Color.Gray
                    )

                    /* 選択時のアンダーバー */
                    Spacer(
                        modifier = Modifier
                            .padding(top = 2.dp)
                            .height(2.dp)
                            .width(20.dp)
                            // アンダーバーの色、未選択時は透過
                            .background(if (selected) Color(0xFF854A2A) else Color.Transparent)
                    )
                }
            )
        },
        // 選択時はクリック不可、誤動作防止の為
        enabled = !selected
    )
}

/**
 * カスタムボタン
 *
 * @param viewModel DisplaySwitchAreaViewModel
 *
 * @return Unit
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChangeDurationDateCustom(viewModel: ExpenditureListViewModel) {

    // 選択、未選択の判定
    val selected = viewModel.dateProperty == DateProperty.CUSTOM.name

    // DatePickerの表示非表示の判定
    val visible = remember { mutableStateOf(false) }

    IconButton(
        onClick = { visible.value = !visible.value },
        content = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                content = {

                    /* ボタンのアイコン */
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = null,
                        // テキストの色、未選択時はグレー
                        tint = if (selected) Color(0xFF854A2A) else Color.Gray
                    )

                    /* 選択時のアンダーバー */
                    Spacer(
                        modifier = Modifier
                            .padding(top = 2.dp)
                            .height(2.dp)
                            .width(20.dp)
                            .background(if (selected) Color(0xFF854A2A) else Color.Transparent)
                    )
                }
            )
        }
    )

    /* DatePickerの制御 */
    if (visible.value) {

        // DatePickerでデフォルトで設定しておく日付
        val state = rememberDateRangePickerState(
            viewModel.customOfStartDate.time,
            viewModel.customOfLastDate.time
        )

        // 選択した開始日を取得
        val getStartDate = state.selectedStartDateMillis?.let { Date(it) } ?: Date()

        // 選択した終了日を取得
        val getLastDate = state.selectedEndDateMillis?.let { Date(it) } ?: Date()

        /* DatePickerのダイアログ */
        DatePickerDialog(
            onDismissRequest = { visible.value = false },
            confirmButton = {},
            content = {
                Box(
                    contentAlignment = Alignment.BottomEnd,
                    content = {

                        /* DatePicker */
                        DateRangePicker(
                            state = state,
                            // カレンダービューのみ許可、テキストフィールドは表示不可にする
                            showModeToggle = false,
                            // DatePickerに表示される日付のフォーマット、M月d日
                            dateFormatter = DatePickerFormatter(selectedDateSkeleton = "Md"),
                            dateValidator = {
                                // 未来日の選択を不可にするためのバリデーション
                                !Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault())
                                    .toLocalDate().isAfter(LocalDate.now())
                            }
                        )

                        /* 日付確定ボタン */
                        Button(
                            onClick = {
                                // DatePickerを閉じる
                                visible.value = false
                                // カスタムを選択状態にする
                                viewModel.dateProperty = DateProperty.CUSTOM.name
                                // 選択した開始日をカスタム日付用の変数に保存
                                viewModel.customOfStartDate = getStartDate
                                // 選択した終了日をカスタム日付用の変数に保存
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
 * 日付の表示（表示期間）
 *
 * @param viewModel: DisplaySwitchAreaViewModel
 *
 * @return Unit
 */
@Composable
private fun ShowDurationDate(viewModel: ExpenditureListViewModel) {
    Text(text = viewModel.durationDateText(), fontSize = 24.sp)
}

/**
 * 前へボタン（過去に移動）
 *
 * @param viewModel: DisplaySwitchAreaViewModel
 *
 * @return Unit
 */
@Composable
private fun PrevButton(viewModel: ExpenditureListViewModel) {

    // ボタンのクリック可不可を判定する変数
    var enabled = true
    if (viewModel.dateProperty == DateProperty.CUSTOM.name) {
        enabled = false
    }

    IconButton(
        onClick = {
            viewModel.onClickSwitchDateButton(switchAction = SwitchDate.PREV)
        },
        enabled = enabled,
        content = {
            Icon(
                imageVector = Icons.Default.KeyboardArrowLeft,
                contentDescription = null,
                // クリック不可の場合は透過
                tint = if (enabled) Color(0xFF854A2A) else Color.Transparent
            )
        }
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
private fun NextButton(viewModel: ExpenditureListViewModel) {

    IconButton(
        onClick = {
            viewModel.onClickSwitchDateButton(switchAction = SwitchDate.NEXT)
        },
        enabled = viewModel.isNextButtonEnabled(),
        content = {
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = null,
                // クリック不可の場合は透過
                tint = if (viewModel.isNextButtonEnabled()) Color(0xFF854A2A) else Color.Transparent
            )
        }
    )
}

/**
 * カテゴリー毎に絞り込み
 *
 * @param viewModel: DisplaySwitchAreaViewModel
 *
 * @return Unit
 */
@Composable
private fun SelectCategoryBox(viewModel: ExpenditureListViewModel) {

    // コンテキストメニュー表示非表示の判定
    val expanded = remember { mutableStateOf(false) }
    // 選択されているカテゴリーのID
    val selectCategoryId = remember { mutableIntStateOf(viewModel.selectCategory) }
    // 選択されているカテゴリー名の表示
    val selectCategoryName = remember { mutableStateOf("すべて") }
    // 全カテゴリー取得
    val categories by viewModel.category.collectAsState(initial = emptyList())
    // 初期表示時の表示用のカテゴリー名を取得
    categories.forEach {
        if (it.id == selectCategoryId.intValue) {
            selectCategoryName.value = it.categoryName
        }
    }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable { expanded.value = !expanded.value },
        content = {
            Text(
                text = selectCategoryName.value,
                modifier = Modifier.padding(start = 10.dp),
                color = Color(0xFF854A2A)
            )
            Icon(
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = "選択アイコン",
                tint = Color(0xFF854A2A)
            )
            DropdownMenu(
                expanded = expanded.value,
                onDismissRequest = { expanded.value = false },
                content = {
                    // 全カテゴリーを検索する
                    DropdownMenuItem(
                        text = { Text(text = "すべて") },
                        onClick = {
                            // コンテキストメニューを非表示
                            expanded.value = false
                            // 選択したカテゴリーIDを格納 表示用
                            // 0の場合はすべて
                            selectCategoryId.intValue = 0
                            // 選択したカテゴリー名を格納 表示用
                            selectCategoryName.value = "すべて"
                            // 選択したカテゴリーIDを格納 絞込用
                            // 0の場合はすべて
                            viewModel.selectCategory = 0
                        }
                    )
                    // 全カテゴリーをコンテキストメニューに表示
                    categories.forEach { selectOption ->
                        DropdownMenuItem(
                            text = { Text(text = selectOption.categoryName) },
                            onClick = {
                                // コンテキストメニューを非表示
                                expanded.value = false
                                // 選択したカテゴリーIDを格納 表示用
                                selectCategoryId.intValue = selectOption.id
                                // 選択したカテゴリー名を格納 表示用
                                selectCategoryName.value = selectOption.categoryName
                                // 選択したカテゴリーIDを格納 絞込用
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
private fun SelectDateSortBox(viewModel: ExpenditureListViewModel) {

    // コンテキストメニュー表示非表示の判定
    val expanded = remember { mutableStateOf(false) }
    // 表示順の判定Booleanで判定
    val sort = remember { mutableStateOf(viewModel.sort) }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable { expanded.value = !expanded.value },
        content = {
            Text(
                text = "日付${if (sort.value) "昇順" else "降順"}",
                modifier = Modifier.padding(start = 10.dp),
                color = Color(0xFF854A2A)
            )
            Icon(
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = "選択アイコン",
                tint = Color(0xFF854A2A)
            )
            DropdownMenu(
                expanded = expanded.value,
                onDismissRequest = { expanded.value = false },
                content = {
                    DropdownMenuItem(
                        text = { Text(text = "日付降順") },
                        onClick = {
                            // コンテキストメニューを非表示
                            expanded.value = false
                            // 選択した表示順を格納 表示用
                            sort.value = false
                            // 選択した表示順を格納 並替え用
                            viewModel.sort = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(text = "日付昇順") },
                        onClick = {
                            // コンテキストメニューを非表示
                            expanded.value = false
                            // 選択した表示順を格納 表示用
                            sort.value = true
                            // 選択した表示順を格納 並替え用
                            viewModel.sort = true
                        }
                    )
                }
            )
        }
    )
}