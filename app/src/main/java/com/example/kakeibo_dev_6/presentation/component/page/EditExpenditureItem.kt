package com.example.kakeibo_dev_6.presentation.component.page

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.kakeibo_dev_6.common.Colors.BASE_COLOR
import com.example.kakeibo_dev_6.presentation.component.parts.SubTopBar
import com.example.kakeibo_dev_6.presentation.component.utility.checkInt
import com.example.kakeibo_dev_6.presentation.component.utility.toDate
import com.example.kakeibo_dev_6.presentation.ScreenRoute
import com.example.kakeibo_dev_6.presentation.viewModel.EditExpenditureItemViewModel
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date
import java.util.Locale

@Composable
fun EditExpenditureItem(
    navController: NavController,
    id: Int? = null,
    viewModel: EditExpenditureItemViewModel = hiltViewModel()
) {
    val yMd = SimpleDateFormat("y年M月d日", Locale.JAPANESE)
    val df = SimpleDateFormat("yyyy-MM-dd", Locale.JAPANESE)
    val payDate = remember { mutableStateOf("") }
    val price = remember { mutableStateOf("") }
    val categoryId = remember { mutableStateOf("") }
    val content = remember { mutableStateOf("") }

    if (id == null) {
        payDate.value = df.format(Date()) + " 12:00:00"
        price.value = ""
        categoryId.value = ""
        content.value = ""
        viewModel.viewPayDate =
            if (viewModel.viewPayDate == "") yMd.format(Date()) else viewModel.viewPayDate
    } else {
        val editExpendItem by viewModel.setEditingExpendItem(id = id).collectAsState(initial = null)
        LaunchedEffect(editExpendItem) {
            val isExpendItem = editExpendItem != null
            payDate.value = if (isExpendItem) editExpendItem!!.payDate + " 12:00:00" else ""
            price.value = if (isExpendItem) editExpendItem!!.price else ""
            categoryId.value = if (isExpendItem) editExpendItem!!.categoryId else ""
            content.value = if (isExpendItem) editExpendItem!!.content else ""
            viewModel.viewPayDate =
                if (isExpendItem) yMd.format(editExpendItem!!.payDate.toDate("yyyy-MM-dd")!!) else ""

            viewModel.editingExpendItem = editExpendItem
        }
    }

    Scaffold(
        topBar = {
            SubTopBar(
                title = if (id == null) "支出項目 追加" else "支出項目 編集",
                navigation = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "閉じる", tint = Color(0xFF854A2A))
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            var validCount = 0

                            if (payDate.value.toDate("yyyy-MM-dd") == null) {
                                validCount++
                                viewModel.inputValidatePayDateStatus = true
                                viewModel.inputValidatePayDateText = "日付を入力してください。"
                            } else {
                                viewModel.inputValidatePayDateStatus = false
                            }

                            if (price.value == "") {
                                validCount++
                                viewModel.inputValidatePriceStatus = true
                                viewModel.inputValidatePriceText = "金額を入力してください。"
                            } else if (!checkInt(price.value)) {
                                validCount++
                                viewModel.inputValidatePriceStatus = true
                                viewModel.inputValidatePriceText = "金額が不正です。"
                            } else {
                                viewModel.inputValidatePriceStatus = false
                            }

                            if (categoryId.value == "") {
                                validCount++
                                viewModel.inputValidateSelectCategoryText = "カテゴリーが未選択です。"
                                viewModel.inputValidateSelectCategoryStatus = true
                            } else {
                                viewModel.inputValidateSelectCategoryStatus = false
                            }

                            if (content.value == "") {
                                validCount++
                                viewModel.inputValidateContentText = "内容が未入力です。"
                                viewModel.inputValidateContentStatus = true
                            } else if (content.value.length > 50) {
                                validCount++
                                viewModel.inputValidateContentText = "内容は50文字以内で入力してください。"
                                viewModel.inputValidateContentStatus = true
                            } else {
                                viewModel.inputValidateContentStatus = false
                            }

                            if (validCount == 0) {
                                navController.popBackStack()
                                viewModel.payDate = payDate.value
                                viewModel.price = price.value
                                viewModel.category_id = categoryId.value
                                viewModel.content = content.value
                                if (id == null) {
                                    viewModel.createExpendItem()
                                } else {
                                    viewModel.updateExpendItem()
                                }
                            }
                        },
                        content = {
                            Icon(imageVector = Icons.Default.Check, contentDescription = "登録", tint = Color(0xFF854A2A))
                        }
                    )
                }
            )
        },
        containerColor = Color(BASE_COLOR),
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            // 日付
            InputPayDate(payDate = payDate, viewModel = viewModel)
            Spacer(modifier = Modifier.height(16.dp))
            // 金額
            InputPrice(price = price, viewModel = viewModel)
            Spacer(modifier = Modifier.height(16.dp))
            // カテゴリー
            InputCategory(
                category_id = categoryId,
                navController = navController,
                viewModel = viewModel,
                id = id
            )
            Spacer(modifier = Modifier.height(16.dp))
            // 内容
            InputContent(content = content, viewModel = viewModel)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun InputPayDate(
    payDate: MutableState<String>,
    viewModel: EditExpenditureItemViewModel
) {

    val yMd = SimpleDateFormat("y年M月d日", Locale.JAPANESE)
    var visible by remember { mutableStateOf(false) }

    Text(
        text = "日付",
        modifier = Modifier.padding(bottom = 4.dp),
        fontWeight = FontWeight.Bold
    )
    Box(
        modifier = Modifier
            .size(280.dp, 50.dp)
            .clip(RoundedCornerShape(4.dp))
            .border(
                BorderStroke(1.dp, Color.LightGray),
                RoundedCornerShape(4.dp)
            )
            .background(Color.White) // 背景色が枠線からはみ出るので背景色のパラメーターはclipとborderの後に設定
            .clickable { visible = !visible },
        contentAlignment = Alignment.CenterStart,
    ) {
        Text(text = viewModel.viewPayDate, fontSize = 16.sp, modifier = Modifier.padding(10.dp))
        Icon(
            imageVector = Icons.Filled.ArrowDropDown,
            contentDescription = "選択アイコン",
            modifier = Modifier.align(Alignment.CenterEnd)
        )
        if (visible) {
            val setState = payDate.value?.let { payDate.value.toDate() } ?: Date()
            val state = rememberDatePickerState(setState.time)
            val getDate = state.selectedDateMillis?.let {
                Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
            }
            DatePickerDialog(
                onDismissRequest = { visible = false },
                confirmButton = {
                    Row(
                        content = {
                            TextButton(
                                onClick = { visible = false },
                                content = { Text(text = "キャンセル") })
                            TextButton(
                                onClick = {
                                    visible = false
                                    payDate.value =
                                        getDate?.let { getDate.toString() + " 12:00:00" }
                                            ?: if (payDate.value == "") LocalDate.now()
                                                .toString() + " 12:00:00" else payDate.value + " 12:00:00"
                                    viewModel.viewPayDate =
                                        yMd.format(payDate.value.toDate("yyyy-MM-dd"))
                                },
                                content = { Text(text = "OK") }
                            )
                        }
                    )
                },
                content = {
                    DatePicker(
                        state = state,
                        showModeToggle = false,
                        dateValidator = {
                                !Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault())
                                    .toLocalDate()
                                    .isAfter(LocalDate.now())
                        }
                    )
                }
            )
        }
    }
    if (viewModel.inputValidatePayDateStatus) {
        Text(
            text = viewModel.inputValidatePayDateText,
            color = Color.Red,
            fontSize = 14.sp
        )
    }
}

@Composable
private fun InputPrice(price: MutableState<String>, viewModel: EditExpenditureItemViewModel) {
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
            .width(280.dp)
    )
    if (viewModel.inputValidatePriceStatus) {
        Text(
            text = viewModel.inputValidatePriceText,
            color = Color.Red,
            fontSize = 14.sp
        )
    }
}

@Composable
private fun InputCategory(
    category_id: MutableState<String>,
    navController: NavController,
    viewModel: EditExpenditureItemViewModel,
    id: Int?
) {

    val categories by viewModel.category.collectAsState(initial = emptyList())
    val expanded = remember { mutableStateOf(false) }
    val selectOptionText = remember { mutableStateOf("カテゴリーを選択してください") }
    categories.forEach {
        if (viewModel.createCategoryFlg) {
            if (it.categoryOrder == viewModel.firstCategory) {
                selectOptionText.value = it.categoryName
                category_id.value = it.id.toString()
            }
        } else {
            if (it.id.toString() == category_id.value) {
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
            categories.forEach {
                DropdownMenuItem(
                    text = { Text(text = it.categoryName) },
                    onClick = {
                        selectOptionText.value = it.categoryName
                        category_id.value = it.id.toString()
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
                    if (id == null) {
                        navController.navigate(ScreenRoute.AddCategoryFromAddExpenditureItem.route)
                    } else {
                        navController.navigate(ScreenRoute.AddCategoryFromEditExpenditureItem.route + "/${id}")
                    }
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
    if (viewModel.inputValidateSelectCategoryStatus) {
        Text(
            text = viewModel.inputValidateSelectCategoryText,
            color = Color.Red,
            fontSize = 14.sp
        )
    }
}

@Composable
private fun InputContent(content: MutableState<String>, viewModel: EditExpenditureItemViewModel) {
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
    if (viewModel.inputValidateContentStatus) {
        Text(
            text = viewModel.inputValidateContentText,
            color = Color.Red,
            fontSize = 14.sp
        )
    }
}