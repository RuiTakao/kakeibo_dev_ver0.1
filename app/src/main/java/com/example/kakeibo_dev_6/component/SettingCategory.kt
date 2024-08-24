package com.example.kakeibo_dev_6.component

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.navArgument
import com.example.kakeibo_dev_6.Category
import com.example.kakeibo_dev_6.MainViewModel
import com.example.kakeibo_dev_6.route.Route
import kotlinx.coroutines.launch

@Composable
fun SettingCategory(navController: NavController, viewModel: MainViewModel = hiltViewModel()) {

    Scaffold(
        topBar = {
            TopBar(navController = navController)
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .background(Color(0xFFF7F7F7))
                .fillMaxSize()
        ) {
            val categories by viewModel.category.collectAsState(initial = emptyList())
            Text(text = viewModel.name)
            LazyColumn {
                items(items = categories) { category ->
                    RowContent(
                        category = category,
                        onClickEdit = {
                            navController.navigate("${Route.EDIT_CATEGORY.name}/${category.id}")
                        },
                        onClickDelete = {
                            viewModel.deleteCategory(it)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun EditPage(id: String? = null) {
    Column {
        id?.let {
            Text(text = id)
        } ?: let {
            Text(text = "nullです")
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(navController: NavController) {
    TopAppBar(
        title = {
            Text(
                text = "カテゴリー設定",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFFF7F7F7)
        ),
        navigationIcon = {
            IconButton(onClick = { navController.navigate(Route.EXPENDITURE_LIST.name) }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "戻る")
            }
        },
        actions = {
            IconButton(onClick = { navController.navigate(Route.EDIT_CATEGORY.name) }) {
                Icon(
                    imageVector = Icons.Outlined.AddCircle,
                    contentDescription = "カテゴリ追加"
                )
            }
        }
    )
}

@Composable
private fun RowContent(
    category: Category,
    onClickEdit: (Category) -> Unit,
    onClickDelete: (Category) -> Unit
) {
    val scope = rememberCoroutineScope()
    var showMenu by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .padding(top = 8.dp)
            .fillMaxWidth()
    ) {

        Row(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .background(Color.White)
                .fillMaxWidth()
                .padding(start = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = category.name)
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
    }
}