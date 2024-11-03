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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun Tab(navController: NavController, page: String, onClick: () -> Unit) {

    val tabSize = LocalConfiguration.current.screenWidthDp.dp / 2

    var mode = true

    if (page != "item") {
        mode = false
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp)
    ) {

        TabRow(tabSize = tabSize, text = "支出項目", mode = mode, onClick = {navController.popBackStack()})

        TabRow(tabSize = tabSize, text = "明細", mode = !mode, onClick = onClick)
    }
}

@Composable
private fun TabRow(tabSize: Dp,text:String, mode: Boolean, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(tabSize)
            .clickable(onClick = onClick, enabled = !mode)
    ) {
        Text(
            text = text,
            color = if (mode) Color(0xFF854A2A) else Color.Gray,
            fontSize = 20.sp,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Spacer(
            modifier = Modifier
                .height(2.dp)
                .width(tabSize)
                .background(color = if (mode) Color(0xFF854A2A) else Color.Gray)
        )
    }
}