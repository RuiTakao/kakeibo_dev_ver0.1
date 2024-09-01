package com.example.kakeibo_dev_6.component.page.expenditureItemList.parts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kakeibo_dev_6.entity.GroupCategory
import com.example.kakeibo_dev_6.MainViewModel
import com.example.kakeibo_dev_6.weekLastDate
import com.example.kakeibo_dev_6.weekStartDate
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.TemporalAdjusters
import java.util.Calendar
import java.util.Date

@Composable
fun ControlContent(
    EditExpendList: List<GroupCategory>,
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
                TotalTax(EditExpendList = EditExpendList)
            }
            NextButton(viewModel = viewModel)
        }

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

// 表示期間切り替えアイコン
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
                        .background(if (viewModel.dateProperty == dateProperty) MaterialTheme.colorScheme.primary else Color.Transparent)
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
    when (viewModel.dateProperty) {
        "day" -> text = Md.format(viewModel.startDate)
        "week" -> text = "${Md.format(viewModel.startDate)} 〜 ${Md.format(viewModel.lastDate)}"
        "month" -> text = M.format(viewModel.startDate)
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
private fun TotalTax(EditExpendList: List<GroupCategory>) {
    var totalTax by remember {
        mutableStateOf(0)
    }
    LaunchedEffect(EditExpendList) {
        var i = 0
        EditExpendList.forEach {
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