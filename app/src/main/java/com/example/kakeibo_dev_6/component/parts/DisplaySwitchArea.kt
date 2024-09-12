package com.example.kakeibo_dev_6.component.parts

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
import java.util.Locale

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
private fun ChangeDurationDateRow(viewModel: DisplaySwitchAreaViewModel) {
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
                    val standardDate =
                        viewModel.standardOfStartDate.toInstant().atZone(ZoneId.systemDefault())
                            .toLocalDate()
                    if (standardDate.isAfter(LocalDate.now())) {
                        viewModel.standardOfStartDate = Date()
                    }
                },
                viewModel = viewModel
            )

            /* 週ボタン */
            ChangeDurationDateText(
                text = "週",
                dateProperty = DateProperty.WEEK.name,
                onClick = {
                    viewModel.dateProperty = DateProperty.WEEK.name
                    val standardDate =
                        viewModel.standardOfStartDate.toInstant().atZone(ZoneId.systemDefault())
                            .toLocalDate()
                    if (standardDate.isAfter(LocalDate.now())) {
                        viewModel.standardOfStartDate = Date()
                    }
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
    viewModel: DisplaySwitchAreaViewModel
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
private fun ChangeDurationDateCustom(viewModel: DisplaySwitchAreaViewModel) {

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
private fun ShowDurationDate(viewModel: DisplaySwitchAreaViewModel) {
    Text(text = durationDateText(viewModel = viewModel), fontSize = 24.sp)
}

/**
 * 表示期間のテキスト
 *
 * @param viewModel: DisplaySwitchAreaViewModel
 *
 * @return String
 */
private fun durationDateText(viewModel: DisplaySwitchAreaViewModel): String {
    // 日付フォーマット
    val formatDate = SimpleDateFormat("M月d日", Locale.JAPANESE)
    val formatMonth = SimpleDateFormat("M月", Locale.JAPANESE)

    when (viewModel.dateProperty) {

        /* 日 */
        DateProperty.DAY.name -> return formatDate.format(viewModel.standardOfStartDate)

        /* 週 */
        DateProperty.WEEK.name -> {
            // 基準日の週の開始日と終了日を求める為、基準日をカレンダークラスのインスタンスに格納する
            val getDate = Calendar.getInstance()
            getDate.time = viewModel.standardOfStartDate
            // 基準日が何曜日か数字で取得
            val dayOfWeek = getDate.get(Calendar.DAY_OF_WEEK)
            // 基準日から曜日番号を減算し、１加算して基準日の週初めの日付を求める
            getDate.add(Calendar.DATE, dayOfWeek * -1 + 1)
            // 開始日を保存、終了日の計算でgetDateの値が変動するので、ここで保存する
            val startDate = getDate.time
            // getDateはここでは、基準日の週初めの日付になっているため、６加算したら週終わりの日付になる
            getDate.add(Calendar.DATE, 6)
            // 終了日を保存
            val lastDate = getDate.time
            return "${formatDate.format(startDate)} 〜 ${formatDate.format(lastDate)}"
        }

        /* 月 */
        // 日付フォーマットで月のみ取得
        DateProperty.MONTH.name -> return formatMonth.format(viewModel.standardOfStartDate)

        /* カスタム */
        // カスタム日付用に保存している開始日と終了日をセット
        DateProperty.CUSTOM.name -> return "${
            formatDate.format(viewModel.customOfStartDate)
        } 〜 ${
            formatDate.format(viewModel.customOfLastDate)
        }"
    }
    return ""
}

/**
 * 前へボタン（過去に移動）
 *
 * @param viewModel: DisplaySwitchAreaViewModel
 *
 * @return Unit
 */
@Composable
private fun PrevButton(viewModel: DisplaySwitchAreaViewModel) {

    // ボタンのクリック可不可を判定する変数
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
private fun NextButton(viewModel: DisplaySwitchAreaViewModel) {

    IconButton(
        onClick = {
            onClickSwitchDateButton(
                viewModel = viewModel,
                switchAction = SwitchDate.NEXT
            )
        },
        enabled = isNextButtonEnabled(viewModel = viewModel),
        content = {
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = null,
                tint = if (isNextButtonEnabled(viewModel = viewModel)) Color(0xFF854A2A) else Color.Transparent
            )
        }
    )
}

/**
 * Nextボタンのクリック不可判定
 *
 * @param viewModel: DisplaySwitchAreaViewModel
 *
 * @return Unit
 */
private fun isNextButtonEnabled(viewModel: DisplaySwitchAreaViewModel): Boolean {

    // 比較用に基準日をLocalDate型にしておく
    val standardDate =
        viewModel.standardOfStartDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()

    if (
        viewModel.dateProperty == DateProperty.CUSTOM.name || // カスタムが選択されている場合はクリック不可
        standardDate.isEqual(LocalDate.now()) // 日付が本日の場合はクリック不可
    ) {
        return false
    } else {
        when (viewModel.dateProperty) {

            // 週が選択されている場合の判定
            DateProperty.WEEK.name -> {

                // 基準日をカレンダーインスタンスに格納する
                val getDate = Calendar.getInstance()
                getDate.time = viewModel.standardOfStartDate
                // 基準日の曜日を数字で取得
                val dayOfWeek = getDate.get(Calendar.DAY_OF_WEEK)
                // 基準日の週の最終日を求める
                getDate.add(Calendar.DATE, dayOfWeek * -1 + 7)
                val weekOfLastDate =
                    getDate.time.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                // 本日の週の最終日が本日以降であればクリック不可
                if (weekOfLastDate.isAfter(LocalDate.now())) {
                    return false
                }
            }

            // 月が選択されている場合の判定
            DateProperty.MONTH.name -> {
                // 本日の月の開始日
                val nowIsFirstDayOfMonth = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth())
                // 基準日の月の開始日
                val standardDayOfFirstDayOfMonth =
                    standardDate.with(TemporalAdjusters.firstDayOfMonth())
                // 本日の月と基準日の月が同じ場合はクリック不可
                if (nowIsFirstDayOfMonth.isEqual(standardDayOfFirstDayOfMonth)) {
                    return false
                }
            }
        }
    }
    return true
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
            getDate.time = viewModel.standardOfStartDate
            val amount =
                when (switchAction) {
                    SwitchDate.PREV -> -7
                    SwitchDate.NEXT -> 7
                }
            getDate.add(Calendar.DATE, amount)
            viewModel.standardOfStartDate = getDate.time
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

    val selectCategory = remember { mutableIntStateOf(viewModel.selectCategory) }
    val selectCategoryName = remember { mutableStateOf("すべて") }
    val expanded = remember { mutableStateOf(false) }
    val categories by viewModel.category.collectAsState(initial = emptyList())
    categories.forEach {
        if (it.id == selectCategory.intValue) {
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
                    DropdownMenuItem(
                        text = { Text(text = "すべて") },
                        onClick = {
                            expanded.value = false
                            selectCategory.intValue = 0
                            selectCategoryName.value = "すべて"
                            viewModel.selectCategory = 0
                        }
                    )
                    categories.forEach { selectOption ->
                        DropdownMenuItem(
                            text = { Text(text = selectOption.categoryName) },
                            onClick = {
                                expanded.value = false
                                selectCategory.intValue = selectOption.id
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