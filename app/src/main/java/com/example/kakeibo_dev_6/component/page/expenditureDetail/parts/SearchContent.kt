package com.example.kakeibo_dev_6.component.page.expenditureDetail.parts

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
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
import com.example.kakeibo_dev_6.MainViewModel
import com.example.kakeibo_dev_6.entity.CategorizeExpenditureItem
import com.example.kakeibo_dev_6.entity.ExpenditureItemJoinCategory
import com.example.kakeibo_dev_6.weekLastDate
import com.example.kakeibo_dev_6.weekStartDate
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.TemporalAdjusters
import java.util.Calendar
import java.util.Date

@Composable
fun SearchContext(
    expenditureItemList: List<ExpenditureItemJoinCategory>,
    viewModel: MainViewModel
) {

    Column(modifier = Modifier.background(color = MaterialTheme.colorScheme.onPrimary)) {
        ChangeDurationDateRow(viewModel = viewModel)
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            PrevButton(viewModel = viewModel)
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                ShowDurationDate(viewModel = viewModel)
                TotalTax(expenditureItemList = expenditureItemList)
            }
            NextButton(viewModel = viewModel)
        }
        Spacer(modifier = Modifier.height(32.dp))
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

// 表示期間切り替えアイコンコンテンツ
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
                viewModel.payDetailStartDate = Date()
                viewModel.payDetailLastDate = Date()
                viewModel.payDetailDateProperty = "day"
            },
            viewModel = viewModel
        )
        ChangeDurationDateText(
            text = "週",
            dateProperty = "week",
            onClick = {
                viewModel.payDetailStartDate = weekStartDate()
                viewModel.payDetailLastDate = weekLastDate()
                viewModel.payDetailDateProperty = "week"
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
                viewModel.payDetailStartDate =
                    Date.from(firstDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
                viewModel.payDetailLastDate =
                    Date.from(lastDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
                viewModel.payDetailDateProperty = "month"
            },
            viewModel = viewModel
        )
        IconButton(onClick = { }, content = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(imageVector = Icons.Default.DateRange, contentDescription = null)
                Spacer(modifier = Modifier.height(2.dp))
                Spacer(
                    modifier = Modifier
                        .height(2.dp)
                        .width(20.dp)
                        .background(Color.Transparent)
                )
            }
        })
    }
}

@Composable
private fun ChangeDurationDateText(
    text: String,
    dateProperty: String,
    onClick: () -> Unit,
    viewModel: MainViewModel
) {
    TextButton(
        onClick = onClick,
        content = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = text, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(2.dp))
                Spacer(
                    modifier = Modifier
                        .height(2.dp)
                        .width(20.dp)
                        .background(if (viewModel.payDetailDateProperty == dateProperty) MaterialTheme.colorScheme.primary else Color.Transparent)
                )
            }
        }
    )
}


// 表示する期間
@Composable
private fun ShowDurationDate(viewModel: MainViewModel) {
    val Md = SimpleDateFormat("M月d日")
    val M = SimpleDateFormat("M月")
    var text = ""
    when (viewModel.payDetailDateProperty) {
        "day" -> text = Md.format(viewModel.payDetailStartDate)
        "week" -> text =
            "${Md.format(viewModel.payDetailStartDate)} 〜 ${Md.format(viewModel.payDetailLastDate)}"

        "month" -> text = M.format(viewModel.payDetailStartDate)
    }
    Text(text = text, fontSize = 24.sp)
}

// prevボタン
@Composable
private fun PrevButton(viewModel: MainViewModel) {
    IconButton(
        onClick = {
            when (viewModel.payDetailDateProperty) {
                "day" -> {
                    val getDate = Calendar.getInstance()
                    getDate.time = viewModel.payDetailStartDate
                    getDate.add(Calendar.DATE, -1)
                    viewModel.payDetailLastDate = getDate.time
                    viewModel.payDetailStartDate = getDate.time
                    Log.d("prev", viewModel.payDetailStartDate.toString())
                }

                "week" -> {
                    val getDate = Calendar.getInstance()
                    getDate.time = viewModel.payDetailStartDate
                    getDate.add(Calendar.DATE, -1)
                    viewModel.payDetailLastDate = getDate.time
                    getDate.add(Calendar.DATE, -6)
                    viewModel.payDetailStartDate = getDate.time
                }

                "month" -> {
                    val getDate = Calendar.getInstance()
                    getDate.time = viewModel.payDetailStartDate
                    getDate.add(Calendar.MONTH, -1)

                    val changeDateToLocalDate =
                        getDate.time.toInstant().atZone(ZoneId.systemDefault())
                            .toLocalDate()
                    val firstDate = changeDateToLocalDate.with(TemporalAdjusters.firstDayOfMonth())
                    val lastDate = changeDateToLocalDate.with(TemporalAdjusters.lastDayOfMonth())
                    viewModel.payDetailStartDate =
                        Date.from(firstDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
                    viewModel.payDetailLastDate =
                        Date.from(lastDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
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
    val lastDate = viewModel.payDetailLastDate.toInstant().atZone(ZoneId.systemDefault())
        .toLocalDate()
    IconButton(
        onClick = {
            when (viewModel.payDetailDateProperty) {
                "day" -> {
                    val getDate = Calendar.getInstance()
                    getDate.time = viewModel.payDetailLastDate
                    getDate.add(Calendar.DATE, 1)
                    viewModel.payDetailLastDate = getDate.time
                    viewModel.payDetailStartDate = getDate.time
                }

                "week" -> {
                    val getDate = Calendar.getInstance()
                    getDate.time = viewModel.payDetailLastDate
                    getDate.add(Calendar.DATE, 1)
                    viewModel.payDetailStartDate = getDate.time
                    getDate.add(Calendar.DATE, 6)
                    viewModel.payDetailLastDate = getDate.time
                }

                "month" -> {
                    val getDate = Calendar.getInstance()
                    getDate.time = viewModel.payDetailStartDate
                    getDate.add(Calendar.MONTH, 1)

                    val changeDateToLocalDate =
                        getDate.time.toInstant().atZone(ZoneId.systemDefault())
                            .toLocalDate()
                    val firstDate = changeDateToLocalDate.with(TemporalAdjusters.firstDayOfMonth())
                    val lastDate = changeDateToLocalDate.with(TemporalAdjusters.lastDayOfMonth())
                    viewModel.payDetailStartDate =
                        Date.from(firstDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
                    viewModel.payDetailLastDate =
                        Date.from(lastDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
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
private fun TotalTax(expenditureItemList: List<ExpenditureItemJoinCategory>) {
    var totalTax by remember {
        mutableStateOf(0)
    }
    LaunchedEffect(expenditureItemList) {
        var i = 0
        expenditureItemList.forEach {
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

// カテゴリのセレクトボックス
@Composable
private fun SelectCategoryBox(viewModel: MainViewModel) {

    val selectCategory = remember { mutableStateOf(viewModel.selectCategory) }
    val selectCategoryName = remember { mutableStateOf("すべて") }
    val expanded = remember { mutableStateOf(false) }
    val categories by viewModel.category.collectAsState(initial = emptyList())

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable { expanded.value = !expanded.value }
    ) {
        Text(text = selectCategoryName.value, modifier = Modifier.padding(start = 10.dp))
        Icon(
            imageVector = Icons.Filled.ArrowDropDown,
            contentDescription = "選択アイコン",
        )
        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false },
        ) {
            DropdownMenuItem(
                text = { Text(text = "すべて") },
                onClick = {
                    expanded.value = false
                    selectCategory.value = 0
                    selectCategoryName.value = "すべて"
                    viewModel.selectCategory = 0
                })
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
    }
}

// 表示順のセレクトボックス
@Composable
private fun SelectDateSortBox(viewModel: MainViewModel) {

    val sort = remember { mutableStateOf(viewModel.sort) }
    val expanded = remember { mutableStateOf(false) }
    
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable { expanded.value = !expanded.value }
    ) {
        Text(
            text = "日付${if (sort.value) "昇順" else "降順"}",
            modifier = Modifier.padding(start = 10.dp)
        )
        Icon(
            imageVector = Icons.Filled.ArrowDropDown,
            contentDescription = "選択アイコン"
        )
        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false }
        ) {
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
    }
}