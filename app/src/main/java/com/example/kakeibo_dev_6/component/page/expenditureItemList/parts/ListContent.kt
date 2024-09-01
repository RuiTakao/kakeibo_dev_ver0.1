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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.kakeibo_dev_6.entity.GroupCategory
import com.example.kakeibo_dev_6.MainViewModel
import com.example.kakeibo_dev_6.route.Route
import java.text.SimpleDateFormat

@Composable
fun ListContent(EditExpendList: List<GroupCategory>, navController: NavController, viewModel: MainViewModel) {
    LazyColumn(
        modifier = Modifier.padding(top = 32.dp),
        content = {
            items(EditExpendList) { expendItem ->
                Item(expendItem = expendItem, navController = navController,viewModel = viewModel)
            }
        }
    )
}

@Composable
private fun Item(expendItem: GroupCategory, navController: NavController, viewModel: MainViewModel) {
    Column(
        modifier = Modifier
            .padding(bottom = 16.dp)
            .clickable {
                val df = SimpleDateFormat("yyyy-MM-dd")
                val startDate = df.format(viewModel.startDate)
                val lastDate = df.format(viewModel.lastDate)
                navController.navigate("${Route.PAY_DETAIL.name}/${expendItem.category_id}/${startDate}/${lastDate}")
            }
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
            Text(text = expendItem.name, fontSize = 20.sp)
            Column(horizontalAlignment = Alignment.End) {
                Text(text = "￥${expendItem.price}", fontSize = 20.sp)
                Text(
                    text = "支出回数：${expendItem.id}回",
                    fontSize = 14.sp,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}