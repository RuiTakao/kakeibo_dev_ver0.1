package com.example.kakeibo_dev_6.component.page.expenditureItemList

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.kakeibo_dev_6.MainViewModel
import com.example.kakeibo_dev_6.component.page.expenditureItemList.parts.ControlContent
import com.example.kakeibo_dev_6.component.page.expenditureItemList.parts.Drawer
import com.example.kakeibo_dev_6.component.page.expenditureItemList.parts.ListContent
import com.example.kakeibo_dev_6.component.parts.FAButton
import com.example.kakeibo_dev_6.component.parts.MainTopBar
import com.example.kakeibo_dev_6.enum.Route
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch
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
                MainTopBar(
                    title = "支出項目",
                    navigation = {
                        IconButton(
                            onClick = {
                                scope.launch {
                                    drawerState.apply {
                                        if (isClosed) open() else close()
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "メニュー",
                                tint = Color.White
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                            val df = SimpleDateFormat("yyyy-MM-dd")
                            val startDate = df.format(viewModel.startDate)
                            val lastDate = df.format(viewModel.lastDate)
                            navController.navigate("${Route.PAY_DETAIL.name}/0/${viewModel.dateProperty}/${startDate}/${lastDate}")
                        }) {
                            Icon(
                                imageVector = Icons.Default.ReceiptLong,
                                contentDescription = "詳細",
                                tint = Color.White
                            )
                        }
                    }
                )
            },
            floatingActionButton = { FAButton(onClick = { navController.navigate(Route.EDIT_EXPENDITURE.name) }) }
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