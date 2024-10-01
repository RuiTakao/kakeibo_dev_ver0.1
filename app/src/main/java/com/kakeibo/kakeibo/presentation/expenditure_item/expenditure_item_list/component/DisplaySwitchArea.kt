package com.kakeibo.kakeibo.presentation.expenditure_item.expenditure_item_list.component

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
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Button
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerFormatter
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DateRangePickerState
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
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
import com.kakeibo.kakeibo.common.enum.DateProperty
import com.kakeibo.kakeibo.common.enum.SwitchDate
import com.kakeibo.kakeibo.domain.model.Category
import com.kakeibo.kakeibo.presentation.expenditure_item.expenditure_item_list.ExpenditureItemListViewModel
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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisplaySwitchArea(
    totalTax: Int,
    viewModel: ExpenditureItemListViewModel,
    searchArea: Boolean = false
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(color = Color.White)
    ) {

        // 日、週、月、カスタムボタンエリア
        // 一行目のレイアウト
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth() // 幅いっぱいに広げたいためfillMaxWidthをmodifierにセットしておく
                .padding(top = 8.dp)
        ) {

            /* 日ボタン */
            ChangeDurationDateText(
                changeDurationDateTextProperty = DateProperty.DAY.name,
                selectedDateProperty = viewModel.dateProperty,
                onClick = {

                    // 選択した日付プロパティをViewModelに保存する
                    viewModel.dateProperty = DateProperty.DAY.name

                    // 日付の移動後、選択期間を変更した場合、基準日が本日以降の日付になってしまう事がある為
                    // 日、週で切り替えた場合は基準日が本日以降になっていないか確認し、修正する
                    viewModel.initStandardDate()
                }
            )

            // 週ボタン
            ChangeDurationDateText(
                changeDurationDateTextProperty = DateProperty.WEEK.name,
                selectedDateProperty = viewModel.dateProperty,
                onClick = {

                    // 選択した日付プロパティをViewModelに保存する
                    viewModel.dateProperty = DateProperty.WEEK.name

                    // 日付の移動後、選択期間を変更した場合、基準日が本日以降の日付になってしまう事がある為
                    // 日、週で切り替えた場合は基準日が本日以降になっていないか確認し、修正する
                    viewModel.initStandardDate()
                }
            )

            // 月ボタン
            ChangeDurationDateText(
                changeDurationDateTextProperty = DateProperty.MONTH.name,
                selectedDateProperty = viewModel.dateProperty,
                onClick = {

                    // 選択した日付プロパティをViewModelに保存する
                    viewModel.dateProperty = DateProperty.MONTH.name
                }
            )

            // カスタムボタン

            // DatePickerの表示非表示の判定
            val visible = remember { mutableStateOf(false) }

            // DatePickerでデフォルトで設定しておく日付
            val state = rememberDateRangePickerState(
                viewModel.customOfStartDate.time,
                viewModel.customOfEndDate.time
            )

            ChangeDurationDateCustom(
                selectedDateProperty = viewModel.dateProperty,
                visible = visible,
                state = state,
                onClick = {

                    // DatePickerを閉じる
                    visible.value = false

                    // 選択した日付プロパティをViewModelに保存する
                    viewModel.dateProperty = DateProperty.CUSTOM.name

                    // 選択した開始日をViewModelのカスタム日付に保存
                    viewModel.customOfStartDate =
                        state.selectedStartDateMillis?.let { Date(it) } ?: Date()

                    // 選択した終了日をViewModelのカスタム日付に保存
                    viewModel.customOfEndDate =
                        state.selectedEndDateMillis?.let { Date(it) } ?: Date()
                }
            )
        }

        // 二行目、三行目のレイアウト
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth() // 幅いっぱいに広げたいためfillMaxWidthをmodifierにセットしておく
                .padding(vertical = 16.dp)
        ) {

            // 前へボタン（過去に移動）
            PrevButton(
                enabled = viewModel.isPrevButtonEnabled(),
                onClick = {

                    // 過去の日付に移動する処理
                    viewModel.onClickSwitchDateButton(switchAction = SwitchDate.PREV)
                }
            )

            // 真ん中のレイアウト
            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                // 日付の表示（表示期間）
                // 二行目のレイアウト
                Text(
                    text = viewModel.durationDateText(),
                    fontSize = 24.sp
                )

                // 合計金額（表示額合計）
                // 三行目のレイアウト
                Text(
                    text = "使用額 ￥${totalTax}",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            // 次へボタン（未来へ移動）
            NextButton(
                enabled = viewModel.isNextButtonEnabled(),
                onClick = {

                    // 未来の日付に移動する処理
                    viewModel.onClickSwitchDateButton(switchAction = SwitchDate.NEXT)
                }
            )
        }

        // 明細画面のみ表示
        // 四行目のレイアウト
        if (searchArea) {

            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp)
                    .fillMaxWidth() // 幅いっぱいに広げたいためfillMaxWidthをmodifierにセットしておく
            ) {

                // 絞り込みドロップダウン（カテゴリー毎）

                // 全カテゴリー取得
                val categoryList by viewModel.category.collectAsState(initial = emptyList())
                SelectCategoryBox(
                    categoryList = categoryList,
                    id = viewModel.selectCategoryId,
                    setId = {

                        // 選択したカテゴリーIDをViewModelに保存する
                        viewModel.selectCategoryId = it
                    }
                )

                // 左右の余白
                Spacer(modifier = Modifier.width(8.dp))

                // 並び替えドロップダウン（登録日付順）
                SelectDateSortBox(
                    sortFlg = viewModel.sortOfPayDate,
                    setSortFlag = {

                        // 選択した支出日の並び順をViewModelに保存する
                        viewModel.sortOfPayDate = it
                    }
                )
            }
        }
    }
}

/**
 * 日、週、月のボタンフォーマット
 *
 * @param changeDurationDateTextProperty String ボタンの種類
 * @param onClick () -> Unit
 * @param selectedDateProperty String
 *
 * @return Unit
 */
@Composable
private fun ChangeDurationDateText(
    changeDurationDateTextProperty: String,
    selectedDateProperty: String,
    onClick: () -> Unit
) {

    // 選択、未選択の判定
    val enabled = selectedDateProperty != changeDurationDateTextProperty

    TextButton(
        onClick = onClick,
        content = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                // ボタンのテキスト
                Text(
                    text = when (changeDurationDateTextProperty) {
                        DateProperty.DAY.name -> "日"
                        DateProperty.WEEK.name -> "週"
                        DateProperty.MONTH.name -> "月"
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
        },
        // 選択時はクリック不可、誤動作防止の為
        enabled = enabled
    )
}

/**
 * カスタムボタン
 *
 * @param selectedDateProperty String
 * @param visible MutableState<Boolean>
 * @param state DateRangePickerState
 * @param onClick () -> Unit
 *
 * @return Unit
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChangeDurationDateCustom(
    selectedDateProperty: String,
    visible: MutableState<Boolean>,
    state: DateRangePickerState,
    onClick: () -> Unit
) {

    // 選択、未選択の判定
    val selected = selectedDateProperty == DateProperty.CUSTOM.name

    IconButton(
        onClick = {
            visible.value = !visible.value
        }
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // ボタンのアイコン
            Icon(
                imageVector = Icons.Default.CalendarMonth,
                contentDescription = null,
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
                        dateRangePickerDateValidator(it)
                    }
                )

                // 日付確定ボタン
                Button(
                    onClick = onClick,
                    modifier = Modifier.padding(16.dp),
                    content = { Text(text = "OK") }
                )
            }
        }
    }
}

/**
 * 未来日と二か月前の月の選択を不可にするためのバリデーション
 *
 * @param selectDates Long 選択できる日にち
 *
 * @return Boolean
 */
private fun dateRangePickerDateValidator(selectDates: Long): Boolean {

    // 先月の月の初めを求める変数
    val calendar = Calendar.getInstance()
    // 今月から減算する
    calendar.add(Calendar.MONTH, -2)
    // 先月の日付
    val prevMonth = calendar.time.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
    // 先月の月の初め
    val prevFirstDayOfMonth = prevMonth.with(TemporalAdjusters.firstDayOfMonth())

    // selectDatesをLocalDateに変換
    val selectDatesToLocalDate =
        Instant.ofEpochMilli(selectDates).atZone(ZoneId.systemDefault()).toLocalDate()

    // Bool値を返す
    return !selectDatesToLocalDate.isAfter(LocalDate.now()) &&
            !selectDatesToLocalDate.isBefore(prevFirstDayOfMonth)
}

/**
 * 前へボタン
 * 用途： 過去の支出項目を表示
 *
 * @param enabled Boolean
 * @param onClick () -> Unit
 *
 * @return Unit
 */
@Composable
private fun PrevButton(enabled: Boolean, onClick: () -> Unit) {

    IconButton(
        onClick = onClick,
        enabled = enabled,
        content = {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
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
 * @param enabled Boolean
 * @param onClick () -> Unit
 *
 * @return Unit
 */
@Composable
private fun NextButton(enabled: Boolean, onClick: () -> Unit) {

    IconButton(
        onClick = onClick,
        enabled = enabled,
        content = {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                // クリック不可の場合は透過
                tint = if (enabled) Color(0xFF854A2A) else Color.Transparent
            )
        }
    )
}

/**
 * カテゴリー毎に絞り込み
 *
 * @param categoryList: List<Category>
 * @param id Int
 * @param setId (Int) -> Unit
 *
 * @return Unit
 */
@Composable
private fun SelectCategoryBox(
    categoryList: List<Category>,
    id: Int,
    setId: (Int) -> Unit
) {

    // コンテキストメニュー表示非表示の判定
    val expanded = remember { mutableStateOf(false) }

    // 選択されているカテゴリーのID
    val selectCategoryId = remember { mutableIntStateOf(id) }

    // 選択されているカテゴリー名の表示
    val selectCategoryName = remember { mutableStateOf("すべて") }

    // 全カテゴリー取得
    // 初期表示時の表示用のカテゴリー名を取得
    categoryList.forEach {
        if (it.id == selectCategoryId.intValue) {
            selectCategoryName.value = it.categoryName
        }
    }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable {
            expanded.value = !expanded.value
        }
    ) {

        // 選択したカテゴリー名
        Text(
            text = selectCategoryName.value,
            modifier = Modifier.padding(start = 10.dp),
            color = Color(0xFF854A2A)
        )

        // カテゴリー選択アイコン
        Icon(
            imageVector = Icons.Filled.ArrowDropDown,
            contentDescription = "選択アイコン",
            tint = Color(0xFF854A2A)
        )

        // ドロップダウンメニュー
        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = {
                expanded.value = false
            }
        ) {

            // 全カテゴリーを検索するアイテム
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
                    setId(0)
                }
            )

            // 全カテゴリーをコンテキストメニューに表示
            categoryList.forEach {

                // 選択したカテゴリーを検索するアイテム
                DropdownMenuItem(
                    text = { Text(text = it.categoryName) },
                    onClick = {

                        // コンテキストメニューを非表示
                        expanded.value = false

                        // 選択したカテゴリーIDを格納 表示用
                        selectCategoryId.intValue = it.id

                        // 選択したカテゴリー名を格納 表示用
                        selectCategoryName.value = it.categoryName

                        // 選択したカテゴリーIDを格納 絞込用
                        setId(it.id)
                    }
                )
            }
        }
    }
}

/**
 * 入力日付の並び替え
 *
 * @param sortFlg Boolean
 * @param setSortFlag (Boolean) -> Unit
 *
 * @return Unit
 */
@Composable
private fun SelectDateSortBox(
    sortFlg: Boolean,
    setSortFlag: (Boolean) -> Unit
) {

    // コンテキストメニュー表示非表示の判定
    val expanded = remember { mutableStateOf(false) }

    // 表示順の判定Booleanで判定
    val sortOfPayDate = remember { mutableStateOf(sortFlg) }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable {

            // コンテキストメニューの表示非表示切り替え
            expanded.value = !expanded.value
        }
    ) {

        // 選択した支出日の並び順
        Text(
            text = "日付${if (sortOfPayDate.value) "昇順" else "降順"}",
            modifier = Modifier.padding(start = 10.dp),
            color = Color(0xFF854A2A)
        )

        // カテゴリー選択アイコン
        Icon(
            imageVector = Icons.Filled.ArrowDropDown,
            contentDescription = "選択アイコン",
            tint = Color(0xFF854A2A)
        )

        // ドロップダウンメニュー
        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = {

                // コンテキストメニューを非表示
                expanded.value = false
            }
        ) {

            // 支出日項目降順にするアイテム
            DropdownMenuItem(
                text = { Text(text = "日付降順") },
                onClick = {

                    // コンテキストメニューを非表示
                    expanded.value = false

                    // 選択した表示順を格納 表示用
                    sortOfPayDate.value = false

                    // 選択した表示順を格納 並替え用
                    setSortFlag(false)
                }
            )

            // 支出日項目昇順にするアイテム
            DropdownMenuItem(
                text = { Text(text = "日付昇順") },
                onClick = {

                    // コンテキストメニューを非表示
                    expanded.value = false

                    // 選択した表示順を格納 表示用
                    sortOfPayDate.value = true

                    // 選択した表示順を格納 並替え用
                    setSortFlag(true)
                }
            )
        }
    }
}