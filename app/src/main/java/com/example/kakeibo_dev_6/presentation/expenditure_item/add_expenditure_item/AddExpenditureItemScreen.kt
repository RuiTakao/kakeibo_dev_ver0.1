package com.example.kakeibo_dev_6.presentation.expenditure_item.add_expenditure_item

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.kakeibo_dev_6.common.Colors
import com.example.kakeibo_dev_6.presentation.ScreenRoute
import com.example.kakeibo_dev_6.presentation.component.expenditure_item.InputCategoryScreen
import com.example.kakeibo_dev_6.presentation.component.expenditure_item.InputContentScreen
import com.example.kakeibo_dev_6.presentation.component.expenditure_item.InputPayDateScreen
import com.example.kakeibo_dev_6.presentation.component.expenditure_item.InputPriceScreen
import com.example.kakeibo_dev_6.presentation.component.SubTopBar
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AddExpenditureItemScreen(
    navController: NavController,
    id: Int? = null,
    viewModel: AddExpenditureItemViewModel = hiltViewModel()
) {
    val yMd = SimpleDateFormat("y年M月d日", Locale.JAPANESE)
    val df = SimpleDateFormat("yyyy-MM-dd", Locale.JAPANESE)
    val payDate = remember { mutableStateOf("") }
    val price = remember { mutableStateOf("") }
    val categoryId = remember { mutableStateOf("") }
    val content = remember { mutableStateOf("") }

    payDate.value = df.format(Date())
    viewModel.viewPayDate =
        if (viewModel.viewPayDate == "") yMd.format(Date()) else viewModel.viewPayDate

    Scaffold(
        topBar = {
            SubTopBar(
                title = if (id == null) "支出項目 追加" else "支出項目 編集",
                navigation = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "閉じる",
                            tint = Color(0xFF854A2A)
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {

                            if (viewModel.validate(
                                    payDate = payDate.value,
                                    price = price.value,
                                    categoryId = categoryId.value,
                                    content = content.value
                                )
                            ) {
                                navController.popBackStack()
                                viewModel.payDate = payDate.value
                                viewModel.price = price.value
                                viewModel.categoryId = categoryId.value
                                viewModel.content = content.value

                                viewModel.createExpendItem()
                            }
                        },
                        content = {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "登録",
                                tint = Color(0xFF854A2A)
                            )
                        }
                    )
                }
            )
        },
        containerColor = Color(Colors.BASE_COLOR),
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(horizontal = 16.dp)
                .padding(top = 32.dp)
        ) {

            // 日付
            InputPayDateScreen(
                payDate = payDate,
                validationMsg = viewModel.inputValidatePayDateText
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 金額
            InputPriceScreen(price = price, validationMsg = viewModel.inputValidatePriceText)

            Spacer(modifier = Modifier.height(16.dp))

            // カテゴリー
            val categoryList by viewModel.category.collectAsState(initial = emptyList())
            InputCategoryScreen(
                categoryId = categoryId,
                categoryList = categoryList,
                addCategoryOrder = viewModel.firstCategory,
                validationMsg = viewModel.inputValidateSelectCategoryText,
                onClickAddCategory = {
                    navController.navigate(ScreenRoute.AddCategoryFromAddExpenditureItem.route)
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 内容
            InputContentScreen(
                content = content,
                validationMsg = viewModel.inputValidateContentText
            )
        }
    }
}