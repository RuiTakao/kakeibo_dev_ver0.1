package com.example.kakeibo_dev_6.component.page.expenditureItemList.parts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.kakeibo_dev_6.enum.Route
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun Drawer(
    navController: NavController,
    drawerState: DrawerState,
    scope: CoroutineScope
) {
    ModalDrawerSheet(
        modifier = Modifier.width(256.dp),
        drawerShape = MaterialTheme.shapes.extraSmall
    ) {
        Column(
            modifier = Modifier
                .background(color = Color(0xFF854A2A))
                .fillMaxWidth()
                .height(64.dp),
            verticalArrangement = Arrangement.Center
        ) {
            IconButton(onClick = {
                scope.launch {
                    drawerState.apply {
                        if (isClosed) open() else close()
                    }
                }
            }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "閉じる",
                    tint = Color.White
                )
            }
        }
        Box(modifier = Modifier.fillMaxSize().background(Color(0xFFF8F5E3))) {
            TextButton(
                onClick = {
                    scope.launch {
                        drawerState.apply { close() }
                    }
                    navController.navigate(Route.CATEGORY_SETTING.name)
                },
                modifier = Modifier.padding(top = 16.dp),
                content = { Text(text = "カテゴリ設定", fontSize = 16.sp, color = Color(0xFF854A2A)) }
            )
        }
    }
}