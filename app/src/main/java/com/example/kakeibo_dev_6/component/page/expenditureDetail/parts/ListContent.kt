package com.example.kakeibo_dev_6.component.page.expenditureDetail.parts

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.kakeibo_dev_6.MainViewModel
import com.example.kakeibo_dev_6.entity.ExpenditureItemJoinCategory
import com.example.kakeibo_dev_6.enum.Route
import java.lang.IllegalArgumentException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun ListContent(
    expenditureItemList: List<ExpenditureItemJoinCategory>,
    navController: NavController,
    viewModel: MainViewModel
) {
    LazyColumn(modifier = Modifier.padding(top = 16.dp)) {
        items(expenditureItemList) { expItem ->
            if (expenditureItemList.indexOf(expItem) == 0) {
                Item(
                    expItem = expItem,
                    navController = navController,
                    titleFlag = true,
                    viewModel = viewModel
                )
            } else {
                if (
                    expenditureItemList.get(expenditureItemList.indexOf(expItem)).payDate !=
                    expenditureItemList.get(expenditureItemList.indexOf(expItem) - 1).payDate
                ) {
                    Item(
                        expItem = expItem,
                        navController = navController,
                        titleFlag = true,
                        viewModel = viewModel
                    )
                } else {
                    Item(expItem = expItem, navController = navController, viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
private fun Item(
    expItem: ExpenditureItemJoinCategory,
    navController: NavController,
    titleFlag: Boolean = false,
    viewModel: MainViewModel
) {
    if (titleFlag) {
        if (viewModel.payDetailDateProperty != "day") {
            val Md = SimpleDateFormat("M月d日")
            Text(
                text = Md.format(expItem.payDate.toDate("yyyy-MM-dd")),
                fontSize = 20.sp,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 4.dp)
                    .padding(top = 16.dp)
            )
        } else {
            Spacer(modifier = Modifier.height(16.dp))
        }
    } else {
        if (viewModel.payDetailDateProperty != "day") {
            Spacer(modifier = Modifier.height(4.dp))
        } else {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
    Column(
        modifier = Modifier
            .clickable {
                navController.navigate(
                    route = "${Route.EXPENDITURE_ITEM_DETAIL.name}/${expItem.id}"
                )
            }
            .background(Color.White)
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .fillMaxWidth()

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column {
                Text(text = expItem.content, fontSize = 20.sp, lineHeight = 0.sp)
                Text(
                    text = expItem.categoryName,
                    modifier = Modifier.padding(vertical = 4.dp),
                    fontSize = 14.sp,
                    lineHeight = 0.sp
                )
            }
            Text(text = "￥${expItem.price}", fontSize = 20.sp)
        }
    }
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