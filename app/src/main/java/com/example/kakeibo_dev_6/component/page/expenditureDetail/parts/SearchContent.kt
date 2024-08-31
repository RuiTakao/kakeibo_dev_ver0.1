package com.example.kakeibo_dev_6.component.page.expenditureDetail.parts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
            Text(text = "カテゴリー： すべて", fontSize = 14.sp, modifier = Modifier.padding(top = 2.dp))
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
}