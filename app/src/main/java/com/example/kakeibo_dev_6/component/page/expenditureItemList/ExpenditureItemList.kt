package com.example.kakeibo_dev_6.component.page.expenditureItemList

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.kakeibo_dev_6.MainViewModel
import com.example.kakeibo_dev_6.component.page.expenditureItemList.parts.ControlContent
import com.example.kakeibo_dev_6.component.page.expenditureItemList.parts.Drawer
import com.example.kakeibo_dev_6.component.page.expenditureItemList.parts.ListContent
import com.example.kakeibo_dev_6.component.page.expenditureItemList.parts.TopBar
import com.example.kakeibo_dev_6.enum.Route
import java.text.SimpleDateFormat

@Composable
fun ExpenditureItemList(
    navController: NavController,
    viewModel: MainViewModel = hiltViewModel()
) {

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
                FloatingActionButton(onClick = { navController.navigate(Route.EDIT_EXPENDITURE.name) }) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "追加")
                }
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .background(color = Color(0xFFF7F7F7))
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