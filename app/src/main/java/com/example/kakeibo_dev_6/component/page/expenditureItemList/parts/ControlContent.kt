package com.example.kakeibo_dev_6.component.page.expenditureItemList.parts

import android.util.Log
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kakeibo_dev_6.MainViewModel
import com.example.kakeibo_dev_6.entity.CategorizeExpenditureItem
import com.example.kakeibo_dev_6.weekLastDate
import com.example.kakeibo_dev_6.weekStartDate
import java.lang.IllegalArgumentException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAdjusters
import java.util.Calendar
import java.util.Date

@Composable
fun ControlContent(
    categorizeExpenditureItem: List<CategorizeExpenditureItem>,
    viewModel: MainViewModel
) {
    Column(modifier = Modifier.background(color = MaterialTheme.colorScheme.onPrimary)) {
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
                TotalTax(categorizeExpenditureItem = categorizeExpenditureItem)
            }
            NextButton(viewModel = viewModel)
        }

    }
}

// 表示期間切り替えアイコンコンテンツ
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChangeDurationDateRow(viewModel: MainViewModel) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    ) {
        ChangeDurationDateText(
            text = "日",
            dateProperty = "day",
            onClick = {
                viewModel.startDate = Date()
                viewModel.lastDate = Date()
                viewModel.dateProperty = "day"
            },
            viewModel = viewModel
        )
        ChangeDurationDateText(
            text = "週",
            dateProperty = "week",
            onClick = {
                viewModel.startDate = weekStartDate()
                viewModel.lastDate = weekLastDate()
                viewModel.dateProperty = "week"
            },
            viewModel = viewModel
        )
        ChangeDurationDateText(
            text = "月",
            dateProperty = "month",
            onClick = {
                val date = LocalDate.now()
                val firstDate = date.with(TemporalAdjusters.firstDayOfMonth())
                val lastDate = date.with(TemporalAdjusters.lastDayOfMonth())
                viewModel.startDate =
                    Date.from(firstDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
                viewModel.lastDate =
                    Date.from(lastDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
                viewModel.dateProperty = "month"
            },
            viewModel = viewModel
        )
        ChangeDurationDateCustom(viewModel = viewModel)
    }
}

// 表示期間切り替えアイコン
@Composable
private fun ChangeDurationDateText(
    text: String,
    dateProperty: String,
    onClick: () -> Unit,
    viewModel: MainViewModel
) {
    val selected = viewModel.dateProperty == dateProperty

    TextButton(
        onClick = onClick,
        content = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = text,
                    fontSize = 16.sp,
                    color = if (selected) MaterialTheme.colorScheme.primary else Color.Gray
                )
                Spacer(modifier = Modifier.height(2.dp))
                Spacer(
                    modifier = Modifier
                        .height(2.dp)
                        .width(20.dp)
                        .background(if (selected) MaterialTheme.colorScheme.primary else Color.Transparent)
                )
            }
        },
        enabled = if (selected) false else true
    )
}

// 表示期間切り替えカスタム
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChangeDurationDateCustom(viewModel: MainViewModel) {

    val selected = viewModel.dateProperty == "custom"

    val isShowCustomDateDialog = remember { mutableStateOf(false) }

    IconButton(
        onClick = { isShowCustomDateDialog.value = true },
        content = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription = null,
                    tint = if (selected) MaterialTheme.colorScheme.primary else Color.Gray
                )
                Spacer(modifier = Modifier.height(2.dp))
                Spacer(
                    modifier = Modifier
                        .height(2.dp)
                        .width(20.dp)
                        .background(if (selected) MaterialTheme.colorScheme.primary else Color.Transparent)
                )
            }
        },
        enabled = if (selected) false else true
    )

    if (isShowCustomDateDialog.value) {

        val startCustomDate = remember { mutableStateOf("") }
        val startViewCustomDate = remember { mutableStateOf("") }
        val startVisible = remember { mutableStateOf(false) }

        val lastCustomDate = remember { mutableStateOf("") }
        val lastViewCustomDate = remember { mutableStateOf("") }
        val lastVisible = remember { mutableStateOf(false) }

        AlertDialog(onDismissRequest = { isShowCustomDateDialog.value = false }) {
            Column(
                modifier = Modifier
                    .background(Color.White)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primary)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = "カレンダー",
                            tint = Color.White
                        )
                        Text(
                            text = "表示期間",
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                ) {
                    TextButton(onClick = { startVisible.value = !startVisible.value }) {
                        Text(text = startViewCustomDate.value)
                    }
                    Text(text = "～")
                    TextButton(onClick = { lastVisible.value = !lastVisible.value }) {
                        Text(text = lastViewCustomDate.value)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp)
                ) {
                    TextButton(onClick = { isShowCustomDateDialog.value = false }) {
                        Text(text = "キャンセル")
                    }
                    TextButton(
                        onClick = {
                            isShowCustomDateDialog.value = false
                            viewModel.dateProperty = "custom"
                            viewModel.startDate = startCustomDate.value.toDate("yyyy-MM-dd")!!
                            viewModel.lastDate = lastCustomDate.value.toDate("yyyy-MM-dd")!!
                        }
                    ) {
                        Text(text = "OK")
                    }
                }
            }
        }

        DatePickerCustomDate(
            visible = startVisible,
            customDate = startCustomDate,
            viewCustomDate = startViewCustomDate,
            setDate = viewModel.startDate
        )

        DatePickerCustomDate(
            visible = lastVisible,
            customDate = lastCustomDate,
            viewCustomDate = lastViewCustomDate,
            setDate = viewModel.lastDate
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerCustomDate(
    visible: MutableState<Boolean>,
    customDate: MutableState<String>,
    viewCustomDate: MutableState<String>,
    setDate: Date
) {
    val state = rememberDatePickerState()
    val getDate = state.selectedDateMillis?.let {
        Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
    }
    val yMd = SimpleDateFormat("y年M月d日")
    val df = SimpleDateFormat("yyyy-MM-dd")
    customDate.value =
        getDate?.let { getDate.toString() } ?: df.format(setDate)
    viewCustomDate.value = yMd.format(customDate.value.toDate("yyyy-MM-dd"))

    if (visible.value) {
        DatePickerDialog(
            onDismissRequest = { visible.value = false },
            confirmButton = {
                TextButton(onClick = { visible.value = false }, content = { Text(text = "OK") })
            },
            content = {
                DatePicker(
                    state = state,
                    dateValidator = {
                        if (
                            Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault())
                                .toLocalDate()
                                .isAfter(LocalDate.now())
                        ) false else true
                    }
                )
            }
        )
    }
}

// 表示する期間
@Composable
private fun ShowDurationDate(viewModel: MainViewModel) {
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
private fun PrevButton(viewModel: MainViewModel) {
    IconButton(
        onClick = {
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
                        getDate.time.toInstant().atZone(ZoneId.systemDefault())
                            .toLocalDate()
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
        }
    ) {
        Icon(
            imageVector = Icons.Default.KeyboardArrowLeft,
            contentDescription = null
        )
    }
}

// nextボタン
@Composable
private fun NextButton(viewModel: MainViewModel) {
    val lastDate = viewModel.lastDate.toInstant().atZone(ZoneId.systemDefault())
        .toLocalDate()
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
                        getDate.time.toInstant().atZone(ZoneId.systemDefault())
                            .toLocalDate()
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
        enabled = if (lastDate.isEqual(LocalDate.now())) false else true,
    ) {
        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = null
        )
    }

}

// 支出合計
@Composable
private fun TotalTax(categorizeExpenditureItem: List<CategorizeExpenditureItem>) {
    var totalTax by remember {
        mutableStateOf(0)
    }
    LaunchedEffect(categorizeExpenditureItem) {
        var i = 0
        categorizeExpenditureItem.forEach {
            i += it.price.toInt()
        }
        totalTax = i
    }

    Text(
        text = "使用額 ￥${totalTax}",
        fontSize = 16.sp,
        modifier = Modifier.padding(top = 8.dp)
    )
}

private fun String.toDate(pattern: String = "yyyy-MM-dd HH:mm:ss"): Date? {
    val format = try {
        SimpleDateFormat(pattern)
    } catch (e: IllegalArgumentException) {
        null
    }
    val date = format?.let {
        try {
            it.parse(this)
        } catch (e: ParseException) {
            null
        }
    }
    return date
}