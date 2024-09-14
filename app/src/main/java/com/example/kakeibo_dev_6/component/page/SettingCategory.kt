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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.AddBox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.kakeibo_dev_6.component.parts.SubTopBar
import com.example.kakeibo_dev_6.entity.Category
import com.example.kakeibo_dev_6.enum.Route
import com.example.kakeibo_dev_6.viewModel.SettingCategoryViewModel
import kotlinx.coroutines.launch

@Composable
fun SettingCategory(
    navController: NavController,
    viewModel: SettingCategoryViewModel = hiltViewModel()
) {

    val categories by viewModel.categoryList.collectAsState(initial = emptyList())

    viewModel.standardOrderCategory = categories

    Scaffold(
        topBar = {
            SubTopBar(
                title = "カテゴリー設定",
                navigation = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "戻る", tint = Color(0xFF854A2A))
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate(Route.EDIT_CATEGORY.name) }) {
                        Icon(
                            imageVector = Icons.Outlined.AddBox,
                            contentDescription = "カテゴリ追加",
                            tint = Color(0xFF854A2A)
                        )
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .background(Color(0xFFEEDCB3))
                .fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            LazyColumn {
                items(items = categories) {
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 12.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color.White)
                            .fillMaxWidth()
                            .padding(start = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = it.categoryName, fontSize = 20.sp)
                        SettingDropDownMenu(
                            category = it,
                            onClickEdit = {
                                navController.navigate("${Route.EDIT_CATEGORY.name}/${it.id}")
                            },
                            onClickReplaceOrderCategory = { navController.navigate(Route.REPLACE_ORDER_CATEGORY.name) }
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
private fun SettingDropDownMenu(
    category: Category,
    onClickEdit: (Category) -> Unit,
    onClickReplaceOrderCategory: () -> Unit
) {
    val scope = rememberCoroutineScope()
    var showMenu by remember { mutableStateOf(false) }

    IconButton(onClick = {
        scope.launch {
            showMenu.apply {
                showMenu = true
            }
        }
    }) {
        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = "編集"
        )
        DropdownMenu(
            expanded = showMenu,
            onDismissRequest = { showMenu = false },
        ) {
            TextButton(
                onClick = {
                    showMenu = false
                    onClickEdit(category)
                },
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                Text(text = "編集")
            }
            TextButton(
                onClick = {
                    showMenu = false
                    onClickReplaceOrderCategory()
                },
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                Text(text = "並替え")
            }
        }
    }
}
