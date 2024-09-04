package com.example.kakeibo_dev_6.component.page.settingCategory.parts

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.AddBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavController
import com.example.kakeibo_dev_6.enum.Route

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navController: NavController) {
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
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "戻る")
            }
        },
        actions = {
            IconButton(onClick = { navController.navigate(Route.EDIT_CATEGORY.name) }) {
                Icon(
                    imageVector = Icons.Outlined.AddBox,
                    contentDescription = "カテゴリ追加"
                )
            }
        }
    )
}