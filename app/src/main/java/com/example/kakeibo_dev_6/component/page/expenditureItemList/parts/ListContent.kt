package com.example.kakeibo_dev_6.component.page.expenditureItemList.parts

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.kakeibo_dev_6.MainViewModel
import com.example.kakeibo_dev_6.entity.CategorizeExpenditureItem
import com.example.kakeibo_dev_6.enum.Route
import java.text.SimpleDateFormat

@Composable
fun ListContent(
    categorizeExpenditureItem: List<CategorizeExpenditureItem>,
    navController: NavController,
    viewModel: MainViewModel
) {
    LazyColumn(modifier = Modifier.padding(top = 32.dp), content = {
        items(categorizeExpenditureItem) {
            Item(
                categorizeExpenditureItem = it,
                navController = navController,
                viewModel = viewModel
            )
        }
    })
}

@Composable
private fun Item(
    categorizeExpenditureItem: CategorizeExpenditureItem,
    navController: NavController,
    viewModel: MainViewModel
) {
    Column(modifier = Modifier
        .padding(bottom = 16.dp)
        .clickable {
            val df = SimpleDateFormat("yyyy-MM-dd")
            val startDate = df.format(viewModel.startDate)
            val lastDate = df.format(viewModel.lastDate)
            navController.navigate(
                "${Route.PAY_DETAIL.name}/${categorizeExpenditureItem.id}/${viewModel.dateProperty}/${startDate}/${lastDate}"
            )
        }
        .shadow(elevation = 5.dp)
        .background(Color.White)
        .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 4.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = categorizeExpenditureItem.categoryName, fontSize = 20.sp)
            Column(horizontalAlignment = Alignment.End) {
                Text(text = "￥${categorizeExpenditureItem.price}", fontSize = 20.sp)
                Text(
                    text = "支出回数：${categorizeExpenditureItem.categoryId}回",
                    fontSize = 14.sp,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}