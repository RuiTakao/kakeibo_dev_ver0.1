package com.example.kakeibo_dev_6.presentation.component.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.kakeibo_dev_6.common.Colors.BASE_COLOR
import com.example.kakeibo_dev_6.presentation.component.parts.SubTopBar
import com.example.kakeibo_dev_6.presentation.viewModel.EditCategoryViewModel
import com.example.kakeibo_dev_6.presentation.viewModel.EditExpenditureItemViewModel

/**
 * カテゴリー追加・編集
 *
 * @param navController NavController
 * @param id Int?
 * @param viewModel EditCategoryViewModel
 * @param editExpenditureItemViewModel EditExpenditureItemViewModel?
 *
 * @return Unit
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditCategory(
    navController: NavController,
    id: Int? = null,
    viewModel: EditCategoryViewModel = hiltViewModel(),
    editExpenditureItemViewModel: EditExpenditureItemViewModel? = null
) {

    val categoryList by viewModel.categoryList.collectAsState(initial = null)

    // カテゴリーの最後尾取得
    val maxOrderCategory by viewModel.maxOrderCategory.collectAsState(initial = null)

    val isUsedCategory by viewModel.isUsedCategory(categoryId = id ?: 0)
        .collectAsState(initial = null)

    var value by remember { mutableStateOf("") }
    if (id != null) {
        val category by viewModel.setEditingCategory(id = id).collectAsState(initial = null)
        LaunchedEffect(category) {
            value = if (category != null) category!!.categoryName else ""
            viewModel.editingCategory = category
        }
    }

    val isShowDialog = remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            SubTopBar(
                title = if (id == null) "カテゴリー追加" else "カテゴリー編集",
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

                            if (value == "") { // バリデーション、カテゴリー名未入力
                                viewModel.inputValidateCategoryText = "カテゴリーが未入力です。"
                                viewModel.inputValidateCategoryStatus = true
                            } else if (value.length > 10) { // バリデーション、カテゴリー名10文字以上
                                viewModel.inputValidateCategoryText = "カテゴリーは10文字以内で入力してください。"
                                viewModel.inputValidateCategoryStatus = true
                            } else {

                                var duplicationCategoryFlg = false

                                // バリデーション、カテゴリー名重複
                                try {
                                    categoryList!!.forEach {
                                        if (it.categoryName == value) {
                                            if (id != null && id == it.id) {
                                                return@forEach
                                            }
                                            viewModel.inputValidateCategoryText =
                                                "カテゴリー名が重複しています。"
                                            viewModel.inputValidateCategoryStatus = true
                                            duplicationCategoryFlg = true
                                        }
                                    }
                                } catch (e: NullPointerException) {
                                    viewModel.inputValidateCategoryText =
                                        "重大なエラーが発生しました、開発者に問い合わせしてください。"
                                    viewModel.inputValidateCategoryStatus = true
                                    duplicationCategoryFlg = true
                                }

                                // バリデーションに引っかからなかったら登録・更新処理実行
                                if (!duplicationCategoryFlg) {
                                    viewModel.inputValidateCategoryStatus = false
                                    if (id == null) {
                                        maxOrderCategory?.let {
                                            viewModel.order = maxOrderCategory!!.categoryOrder + 1
                                        } ?: {
                                            viewModel.order = 0
                                        }
                                        if (editExpenditureItemViewModel != null) {
                                            editExpenditureItemViewModel.firstCategory =
                                                viewModel.order
                                            editExpenditureItemViewModel.createCategoryFlg = true
                                        }
                                        viewModel.createCategory()
                                    } else {
                                        viewModel.updateCategory()
                                    }
                                    navController.popBackStack()
                                }
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
        containerColor = Color(BASE_COLOR),
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
            if (viewModel.inputValidateCategoryStatus) {
                Text(
                    text = viewModel.inputValidateCategoryText,
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            if (id != null) {
                val check = isUsedCategory?.size ?: 0
                Spacer(modifier = Modifier.height(56.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { isShowDialog.value = true },
                        enabled = !(check > 0 || id == 1)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "削除",
                            tint = if (check > 0 || id == 1) Color.Gray else Color.Red
                        )
                    }
                    if (check > 0) {
                        Text(
                            text = "このカテゴリーは使用されているため\n削除出来ません",
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    } else if (id == 1) {
                        Text(
                            text = "このカテゴリーは削除出来ません",
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    }
                }

                if (isShowDialog.value) {
                    AlertDialog(onDismissRequest = { isShowDialog.value = !isShowDialog.value }) {
                        Column(
                            modifier = Modifier
                                .background(
                                    color = MaterialTheme.colorScheme.background,
                                    shape = MaterialTheme.shapes.extraLarge
                                ),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(text = "${value}を削除しますか？")
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                horizontalArrangement = Arrangement.End
                            ) {
                                TextButton(onClick = { isShowDialog.value = false }) {
                                    Text(text = "キャンセル")
                                }
                                TextButton(
                                    onClick = {
                                        navController.popBackStack()
                                        viewModel.deleteCategory(viewModel.editingCategory!!)
                                    }
                                ) {
                                    Text(text = "削除", color = Color.Red)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}