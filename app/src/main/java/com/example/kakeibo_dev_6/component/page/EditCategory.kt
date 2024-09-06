package com.example.kakeibo_dev_6.component.page

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
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.kakeibo_dev_6.viewModel.EditCategoryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditCategory(
    navController: NavController,
    id: Int? = null,
    viewModel: EditCategoryViewModel = hiltViewModel()
) {

    var value by remember { mutableStateOf("") }
    if (id == null) {
        value = ""
    } else {
        val category by viewModel.setEditingCategory(id = id).collectAsState(initial = null)
        LaunchedEffect(category) {
            value = if (category != null) category!!.categoryName else ""
            viewModel.editingCategory = category
        }
    }

    val isShowDialog = remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopBar(value = value, id = id, navController = navController, viewModel = viewModel)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(Color(0xFFF8F5E3))
        ) {
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
                    .background(Color.White)
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
                Spacer(modifier = Modifier.height(56.dp))
                IconButton(onClick = { isShowDialog.value = true }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "削除",
                        tint = Color.Red
                    )
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
                                TextButton(onClick = {
                                    navController.popBackStack()
                                    viewModel.deleteCategory(viewModel.editingCategory!!)
                                }) {
                                    Text(text = "OK")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(value: String, id: Int?, navController: NavController, viewModel: EditCategoryViewModel) {
    TopAppBar(
        title = {
            Text(
                text = "カテゴリ編集",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFFF8F5E3)
        ),
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "閉じる")
            }
        },
        actions = {
            IconButton(
                onClick = {
                    viewModel.name = value
                    if (value == "") {
                        viewModel.inputValidateCategoryText = "カテゴリーが未入力です。"
                        viewModel.inputValidateCategoryStatus = true
                    } else if (value.length > 10) {
                        viewModel.inputValidateCategoryText = "カテゴリーは10文字以内で入力してください。"
                        viewModel.inputValidateCategoryStatus = true
                    } else {
                        viewModel.inputValidateCategoryStatus = false
                        if (id == null) {
                            viewModel.order = 1
                            viewModel.createCategory()
                        } else {
                            viewModel.updateCategory()
                        }
                        navController.popBackStack()
                    }
                }
            ) {
                Icon(imageVector = Icons.Default.Check, contentDescription = "登録")
            }
        }
    )
}