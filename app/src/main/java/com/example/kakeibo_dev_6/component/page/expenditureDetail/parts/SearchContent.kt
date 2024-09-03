package com.example.kakeibo_dev_6.component.page.expenditureDetail.parts

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kakeibo_dev_6.MainViewModel
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.TemporalAdjusters
import java.util.Calendar
import java.util.Date

@Composable
fun SearchContext(isShowSearchDialog: MutableState<Boolean>, viewModel: MainViewModel) {

    Box(
        modifier = Modifier
            .padding(top = 8.dp)
            .background(color = Color(0xFFD9D9D9), shape = RoundedCornerShape(8))
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp)
        ) {
            val df = SimpleDateFormat("M月d日")
            Text(
                text = "表示期間： ${df.format(viewModel.payDetailStartDate)} 〜 ${
                    df.format(
                        viewModel.payDetailLastDate
                    )
                }", fontSize = 14.sp
            )
            Text(text = "カテゴリー： ${viewModel.selectCategoryName}", fontSize = 14.sp, modifier = Modifier.padding(top = 2.dp))
            Text(
                text = "表示順： 日付${if (viewModel.sort) "昇順" else "降順"}",
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
        IconButton(
            modifier = Modifier.align(Alignment.TopEnd),
            onClick = { isShowSearchDialog.value = !isShowSearchDialog.value }) {
            Icon(imageVector = Icons.Default.MoreHoriz, contentDescription = "検索")
        }
    }

    Column(modifier = Modifier.background(color = MaterialTheme.colorScheme.onPrimary)) {
//        ChangeDurationDateRow(viewModel = viewModel)
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            PrevButton(viewModel = viewModel)
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                ShowDurationDate(viewModel = viewModel)
//                TotalTax(categorizeExpenditureItem = categorizeExpenditureItem)
            }
            NextButton(viewModel = viewModel)
        }

    }
}

// prevボタン
@Composable
private fun PrevButton(viewModel: MainViewModel) {
    Log.d("prev", viewModel.payDetailDateProperty)
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
    Log.d("next", viewModel.payDetailDateProperty)
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