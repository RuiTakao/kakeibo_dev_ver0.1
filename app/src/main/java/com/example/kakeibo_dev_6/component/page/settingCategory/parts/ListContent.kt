package com.example.kakeibo_dev_6.component.page.settingCategory.parts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.kakeibo_dev_6.entity.Category
import com.example.kakeibo_dev_6.MainViewModel
import com.example.kakeibo_dev_6.enum.Route
import kotlinx.coroutines.launch

@Composable
fun ListContent(navController: NavController, viewModel: MainViewModel) {
    val categories by viewModel.category.collectAsState(initial = emptyList())
    LazyColumn {
        items(items = categories) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .background(Color.White)
                    .fillMaxWidth()
                    .padding(start = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = it.categoryName)
                SettingDropDownMenu(
                    category = it,
                    onClickEdit = {
                        navController.navigate("${Route.EDIT_CATEGORY.name}/${it.categoryId}")
                    },
                    onClickDelete = {
                        viewModel.deleteCategory(it)
                    }
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun SettingDropDownMenu(
    category: Category,
    onClickEdit: (Category) -> Unit,
    onClickDelete: (Category) -> Unit
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
                    onClickDelete(category)
                },
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                Text(text = "削除", color = Color.Red)
            }
        }
    }
}
