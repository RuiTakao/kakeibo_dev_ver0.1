package com.example.kakeibo_dev_6.component.page.expenditureDetail.parts

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kakeibo_dev_6.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchDialog(isShowSearchDialog: MutableState<Boolean>, viewModel: MainViewModel) {
    if (isShowSearchDialog.value) {
        val sort = remember { mutableStateOf(viewModel.sort) }
        val selectCategory = remember { mutableStateOf(viewModel.selectCategory) }
        val selectCategoryName = remember { mutableStateOf(viewModel.selectCategoryName) }

        AlertDialog(
            onDismissRequest = { isShowSearchDialog.value = false },
            modifier = Modifier
                .background(color = Color.White)
                .padding(horizontal = 24.dp)
        ) {
            Column {
                Column(
                    modifier = Modifier.padding(top = 24.dp)
                ) {
                    DateDurationField()
                    Spacer(modifier = Modifier.height(16.dp))
                    SelectCategoryBox(
                        selectCategory = selectCategory,
                        selectCategoryName = selectCategoryName,
                        viewModel = viewModel
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    SelectDateSortBox(value = sort)
                }
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = { isShowSearchDialog.value = false }) {
                        Text(text = "キャンセル")
                    }
                    TextButton(onClick = {
                        isShowSearchDialog.value = false
                        viewModel.sort = sort.value
                        viewModel.selectCategory = selectCategory.value
                        viewModel.selectCategoryName = selectCategoryName.value
                    }) {
                        Text(text = "OK")
                    }
                }
            }
        }
    }
}

// 表示期間のテキストボックス
@Composable
private fun DateDurationField() {
    var value = ""
    Text(
        text = "表示期間",
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(bottom = 4.dp)
    )
    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = Modifier
            .size(260.dp, 50.dp)
            .drawBehind {
                drawLine(
                    color =  Color.Gray,
                    start =  Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = 2.dp.toPx()
                )
            }
            .background(color = Color.LightGray)
            .clickable { }
    ) {
        Text(
            text = "2024年12月25日 ～ 2024年12月31日",
            fontSize = 14.sp,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

// カテゴリのセレクトボックス
@Composable
private fun SelectCategoryBox(
    selectCategory: MutableState<Int>,
    selectCategoryName: MutableState<String>,
    viewModel: MainViewModel
) {
    val expanded = remember { mutableStateOf(false) }
    val categories by viewModel.category.collectAsState(initial = emptyList())
    Text(
        text = "カテゴリ",
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(bottom = 4.dp)
    )
    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = Modifier
            .size(260.dp, 50.dp)
            .clip(RoundedCornerShape(4.dp))
            .border(
                BorderStroke(1.dp, Color.LightGray),
                RoundedCornerShape(4.dp)
            )
            .clickable { expanded.value = !expanded.value }
    ) {
        Text(text = selectCategoryName.value, modifier = Modifier.padding(start = 10.dp))
        Icon(
            imageVector = Icons.Filled.ArrowDropDown,
            contentDescription = "選択アイコン",
            modifier = Modifier.align(Alignment.CenterEnd)
        )
        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false },
            modifier = Modifier.width(250.dp)
        ) {
            DropdownMenuItem(text = { Text(text = "すべて") }, onClick = {
                expanded.value = false
                selectCategory.value = 0
                selectCategoryName.value = "すべて"
            })
            categories.forEach { selectOption ->
                DropdownMenuItem(text = { Text(text = selectOption.name) }, onClick = {
                    expanded.value = false
                    selectCategory.value = selectOption.id
                    selectCategoryName.value = selectOption.name
                })
            }
        }
    }
}

// 表示順のセレクトボックス
@Composable
private fun SelectDateSortBox(value: MutableState<Boolean>) {
    val expanded = remember { mutableStateOf(false) }
    Text(
        text = "表示順",
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(bottom = 4.dp)
    )
    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = Modifier
            .size(260.dp, 50.dp)
            .clip(RoundedCornerShape(4.dp))
            .border(
                BorderStroke(1.dp, Color.LightGray),
                RoundedCornerShape(4.dp)
            )
            .clickable { expanded.value = !expanded.value }
    ) {
        Text(
            text = "日付${if (value.value) "昇順" else "降順"}",
            modifier = Modifier.padding(start = 10.dp)
        )
        Icon(
            imageVector = Icons.Filled.ArrowDropDown,
            contentDescription = "選択アイコン",
            modifier = Modifier.align(Alignment.CenterEnd)

        )
        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false },
            modifier = Modifier.width(260.dp)
        ) {
            DropdownMenuItem(
                text = { Text(text = "日付降順") },
                onClick = {
                    expanded.value = false
                    value.value = false
                }
            )
            DropdownMenuItem(
                text = { Text(text = "日付昇順") },
                onClick = {
                    expanded.value = false
                    value.value = true
                }
            )
        }
    }
}