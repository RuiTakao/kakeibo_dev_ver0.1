package com.example.kakeibo_dev_6.presentation.component.expenditure_item

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kakeibo_dev_6.domain.model.Category

@Composable
fun InputCategoryScreen(
    categoryId: MutableState<String>,
    categoryList: List<Category>,
    addCategoryOrder: Int,
    validationMsg: String,
    onClickAddCategory: () -> Unit
) {

    val expanded = remember { mutableStateOf(false) }
    val selectOptionText = remember { mutableStateOf("カテゴリーを選択してください") }
    categoryList.forEach {
        if (addCategoryOrder > 0) {
            if (it.categoryOrder == addCategoryOrder) {
                selectOptionText.value = it.categoryName
                categoryId.value = it.id.toString()
            }
        } else {
            if (it.id.toString() == categoryId.value) {
                selectOptionText.value = it.categoryName
            }
        }
    }

    Text(
        text = "カテゴリー",
        modifier = Modifier.padding(bottom = 4.dp),
        fontWeight = FontWeight.Bold
    )
    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = Modifier
            .size(280.dp, 50.dp)
            .clip(RoundedCornerShape(4.dp))
            .border(
                BorderStroke(1.dp, Color.LightGray),
                RoundedCornerShape(4.dp)
            )
            .background(Color.White) // 背景色が枠線からはみ出るので背景色のパラメーターはclipとborderの後に設定
            .clickable { expanded.value = !expanded.value }
    ) {
        Text(text = selectOptionText.value, modifier = Modifier.padding(start = 10.dp))
        Icon(
            imageVector = Icons.Filled.ArrowDropDown,
            contentDescription = "選択アイコン",
            modifier = Modifier.align(Alignment.CenterEnd)
        )

        DropdownMenu(
            expanded = expanded.value,
            modifier = Modifier.width(260.dp),
            onDismissRequest = { expanded.value = false }
        ) {
            categoryList.forEach {
                DropdownMenuItem(
                    text = { Text(text = it.categoryName) },
                    onClick = {
                        selectOptionText.value = it.categoryName
                        categoryId.value = it.id.toString()
                        expanded.value = false
                    }
                )
                Spacer(
                    modifier = Modifier
                        .height(1.dp)
                        .fillMaxWidth()
                        .background(Color.LightGray)
                )
            }
            DropdownMenuItem(
                text = {
                    Row {
                        Icon(
                            imageVector = Icons.Default.AddCircle,
                            contentDescription = "カテゴリー追加"
                        )
                        Text(text = "カテゴリー追加", modifier = Modifier.padding(start = 8.dp))
                    }
                },
                onClick = {
                    expanded.value = false
                    onClickAddCategory()
                },
                modifier = Modifier
                    .background(Color.White)
                    .height(56.dp)
            )
            Spacer(
                modifier = Modifier
                    .height(1.dp)
                    .fillMaxWidth()
                    .background(Color.LightGray)
            )
        }
    }
    if (validationMsg != "") {
        Text(
            text = validationMsg,
            color = Color.Red,
            fontSize = 14.sp
        )
    }
}