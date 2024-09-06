package com.example.kakeibo_dev_6.component.page.expenditureItemList

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.kakeibo_dev_6.MainViewModel
import com.example.kakeibo_dev_6.component.page.expenditureItemList.parts.ControlContent
import com.example.kakeibo_dev_6.component.page.expenditureItemList.parts.Drawer
import com.example.kakeibo_dev_6.component.page.expenditureItemList.parts.ListContent
import com.example.kakeibo_dev_6.component.page.expenditureItemList.parts.TopBar
import com.example.kakeibo_dev_6.enum.Route
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import java.text.SimpleDateFormat

@Composable
fun ExpenditureItemList(
    navController: NavController,
    viewModel: MainViewModel = hiltViewModel()
) {

    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setStatusBarColor(color = Color(0xFF854A2A))
    }

    // 支出項目取得
    val df = SimpleDateFormat("yyyy-MM-dd")
    val categorizeExpenditureItem by viewModel.categorizeExpenditureItem(
        firstDay = df.format(viewModel.startDate),
        lastDay = df.format(viewModel.lastDate)
    ).collectAsState(initial = emptyList())

    // ドロワーの操作用の変数
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            Drawer(
                navController = navController,
                drawerState = drawerState,
                scope = scope
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopBar(
                    drawerState = drawerState,
                    scope = scope,
                    navController = navController,
                    viewModel = viewModel
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { navController.navigate(Route.EDIT_EXPENDITURE.name) },
                    containerColor = Color(0xFF854A2A)
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "追加", tint = Color.White, modifier = Modifier.size(32.dp))
                }
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .background(color = Color(0xFFF8F5E3))
                    .fillMaxSize(),
                content = {
                    ControlContent(
                        categorizeExpenditureItem = categorizeExpenditureItem,
                        viewModel = viewModel
                    )
                    ListContent(
                        categorizeExpenditureItem = categorizeExpenditureItem,
                        navController = navController,
                        viewModel = viewModel
                    )
                }
            )
        }
    }
}