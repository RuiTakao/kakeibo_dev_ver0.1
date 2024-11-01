package com.kakeibo.kakeibo_dev_6.presentation.expenditure_item.expenditure_item_list.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kakeibo.kakeibo_dev_6.presentation.expenditure_item.expenditure_item_list.ExpenditureItemListViewModel

@Composable
fun Tab(viewModel: ExpenditureItemListViewModel) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
    ) {

        val tabSize = LocalConfiguration.current.screenWidthDp.dp / 2

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .width(tabSize)
                .clickable(onClick = {viewModel.switchArea = true})
        ) {
            Text(text = "支出項目", color = Color(0xFF854A2A), fontSize = 20.sp, modifier = Modifier.padding(bottom = 4.dp))
            Spacer(modifier = Modifier.height(2.dp).width(tabSize).background(Color(0xFF854A2A)))
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .width(tabSize)
                .clickable(onClick = {viewModel.switchArea = false})
        ) {
            Text(text = "明細", color = Color.Gray, fontSize = 20.sp, modifier = Modifier.padding(bottom = 4.dp))
            Spacer(modifier = Modifier.height(2.dp).width(tabSize).background(Color.Gray))
        }
    }
}