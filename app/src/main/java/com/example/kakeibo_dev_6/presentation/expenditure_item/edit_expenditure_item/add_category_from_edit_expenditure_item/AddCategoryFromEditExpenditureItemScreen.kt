package com.example.kakeibo_dev_6.presentation.expenditure_item.edit_expenditure_item.add_category_from_edit_expenditure_item

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.kakeibo_dev_6.common.Colors
import com.example.kakeibo_dev_6.presentation.component.parts.SubTopBar
import com.example.kakeibo_dev_6.presentation.expenditure_item.add_expenditure_item.AddExpenditureItemViewModel
@Composable
fun AddCategoryFromEditExpenditureItemScreen(
    navController: NavController,
    addExpenditureItemViewModel: AddExpenditureItemViewModel,
    viewModel: AddCategoryFromEditExpenditureItemViewModel = hiltViewModel()
) {
    var value by remember { mutableStateOf("") }

    // カテゴリーの最後尾取得
    val maxOrderCategory by viewModel.maxOrderCategory.collectAsState(initial = null)

    val categoryList by viewModel.categoryList.collectAsState(initial = null)

    Scaffold(
        topBar = {
            SubTopBar(
                title = "カテゴリー追加",
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
                            viewModel.name = value

                            // バリデーションに引っかからなかったら登録・更新処理実行
                            if (!viewModel.validate(value, categoryList)) {
                                maxOrderCategory?.let {
                                    viewModel.order = maxOrderCategory!!.categoryOrder + 1
                                } ?: {
                                    viewModel.order = 0
                                }
                                addExpenditureItemViewModel.firstCategory = viewModel.order
                                addExpenditureItemViewModel.createCategoryFlg = true
                                viewModel.createCategory()
                                addExpenditureItemViewModel.categoryId

                                navController.popBackStack()
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
        Column(modifier = Modifier.padding(it)) {
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = value,
                onValueChange = {
                    value = it
                },
                label = { Text(text = "カテゴリー名を入力してください") },
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            )
            if (viewModel.inputValidateCategoryText != "") {
                Text(
                    text = viewModel.inputValidateCategoryText,
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}