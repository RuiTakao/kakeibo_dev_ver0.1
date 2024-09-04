package com.example.kakeibo_dev_6.component.page.editExpenditureItem

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerFormatter
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.kakeibo_dev_6.MainViewModel
import java.lang.IllegalArgumentException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditExpenditureItem(
    navController: NavController,
    id: Int? = null,
    viewModel: MainViewModel = hiltViewModel()
) {
    val payDate = remember { mutableStateOf("") }
    val price = remember { mutableStateOf("") }
    val categoryId = remember { mutableStateOf("") }
    val content = remember { mutableStateOf("") }

    if (id == null) {
        payDate.value = ""
        price.value = ""
        categoryId.value = ""
        content.value = ""
    } else {
        val editExpendItem by viewModel.setEditingExpendItem(id = id).collectAsState(initial = null)
        LaunchedEffect(editExpendItem) {
            val isExpendItem = editExpendItem != null
            payDate.value = if (isExpendItem) editExpendItem!!.payDate else ""
            price.value = if (isExpendItem) editExpendItem!!.price else ""
            categoryId.value = if (isExpendItem) editExpendItem!!.categoryId else ""
            content.value = if (isExpendItem) editExpendItem!!.content else ""

            viewModel.editingExpendItem = editExpendItem
        }
    }

    Scaffold(topBar = {
        TopAppBar(title = {
            Text(
                text = "支出項目編集", maxLines = 1, overflow = TextOverflow.Ellipsis
            )
        }, navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "閉じる")
            }
        }, actions = {
            IconButton(onClick = {
                viewModel.payDate = payDate.value
                viewModel.price = price.value
                viewModel.category_id = categoryId.value
                viewModel.content = content.value
                if (id == null) {
                    viewModel.createExpendItem()
                } else {
                    viewModel.updateExpendItem()
                }
                navController.popBackStack()
            }) {
                Icon(imageVector = Icons.Default.Check, contentDescription = "登録")
            }
        })
    }) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(top = 32.dp)
                .padding(horizontal = 16.dp)
        ) {
            // 日付
            InputPayDate(payDate = payDate)
            Spacer(modifier = Modifier.height(16.dp))
            // 金額
            InputPrice(price = price)
            Spacer(modifier = Modifier.height(16.dp))
            // カテゴリー
            InputCategory(category_id = categoryId, viewModel = viewModel)
            Spacer(modifier = Modifier.height(16.dp))
            // 内容
            InputContent(content = content)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun InputPayDate(payDate: MutableState<String>) {

    var viewPayDate by remember { mutableStateOf("") }
    val state = rememberDatePickerState()
    var visible by remember { mutableStateOf(false) }

    val getDate = state.selectedDateMillis?.let {
        Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
    }
    val yMd = SimpleDateFormat("y年M月d日")
    payDate.value = getDate?.let { getDate.toString() } ?: if (payDate.value == "") LocalDate.now()
        .toString() else payDate.value
    viewPayDate = yMd.format(payDate.value.toDate("yyyy-MM-dd"))

    Text(
        text = "日付",
        modifier = Modifier.padding(bottom = 4.dp),
        fontWeight = FontWeight.Bold
    )
    Box(
        modifier = Modifier
            .size(260.dp, 50.dp)
            .clip(RoundedCornerShape(4.dp))
            .border(
                BorderStroke(1.dp, Color.LightGray),
                RoundedCornerShape(4.dp)
            )
            .clickable { visible = !visible },
        contentAlignment = Alignment.CenterStart,
    ) {
        Text(text = viewPayDate, fontSize = 16.sp, modifier = Modifier.padding(8.dp))
        Icon(
            imageVector = Icons.Filled.ArrowDropDown,
            contentDescription = "選択アイコン",
            modifier = Modifier.align(Alignment.CenterEnd)
        )
        if (visible) {
            DatePickerDialog(
                onDismissRequest = { visible = false },
                confirmButton = {
                    TextButton(onClick = { visible = false }, content = { Text(text = "OK") })
                },
                content = {
                    DatePicker(
                        state = state,
                        dateValidator = {
                            if (
                                Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault())
                                    .toLocalDate()
                                    .isAfter(LocalDate.now())
                            ) false else true
                        }
                    )
                }
            )
        }
    }
}

@Composable
private fun InputPrice(price: MutableState<String>) {
    Text(
        text = "金額",
        modifier = Modifier.padding(bottom = 4.dp),
        fontWeight = FontWeight.Bold
    )
    TextField(
        value = price.value,
        onValueChange = {
            price.value = it
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier
            .background(Color.White)
            .width(260.dp)
    )
}

@Composable
private fun InputCategory(category_id: MutableState<String>, viewModel: MainViewModel) {

    val categories by viewModel.category.collectAsState(initial = emptyList())
    val expanded = remember { mutableStateOf(false) }
    val selectOptionText = remember { mutableStateOf("カテゴリを選択してください") }
    categories.forEach {
        if (it.id.toString() == category_id.value) {
            selectOptionText.value = it.categoryName
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
            .size(260.dp, 50.dp)
            .clip(RoundedCornerShape(4.dp))
            .border(
                BorderStroke(1.dp, Color.LightGray),
                RoundedCornerShape(4.dp)
            )
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
            categories.forEach {
                DropdownMenuItem(
                    text = { Text(text = it.categoryName) },
                    onClick = {
                        selectOptionText.value = it.categoryName
                        category_id.value = it.id.toString()
                        expanded.value = false
                    }
                )
            }
        }
    }
}

@Composable
private fun InputContent(content: MutableState<String>) {
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
            .background(Color.White)
            .width(260.dp)
    )
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