package com.example.kakeibo_dev_6.presentation.component.expenditure_item

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun InputContentScreen(content: MutableState<String>, validationMsg: String) {
    Text(
        text = "内容",
        modifier = Modifier.padding(bottom = 4.dp),
        fontWeight = FontWeight.Bold
    )
    TextField(
        value = content.value,
        onValueChange = {
            content.value = it
        },
        modifier = Modifier
            .width(280.dp)
    )
    if (validationMsg != "") {
        Text(
            text = validationMsg,
            color = Color.Red,
            fontSize = 14.sp
        )
    }
}