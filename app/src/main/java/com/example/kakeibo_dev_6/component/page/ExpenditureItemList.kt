package com.example.kakeibo_dev_6.component.page

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.kakeibo_dev_6.component.parts.DisplaySwitchArea
import com.example.kakeibo_dev_6.component.parts.FAButton
import com.example.kakeibo_dev_6.component.parts.MainTopBar
import com.example.kakeibo_dev_6.entity.CategorizeExpenditureItem
import com.example.kakeibo_dev_6.enum.DateProperty
import com.example.kakeibo_dev_6.enum.Route
import com.example.kakeibo_dev_6.viewModel.DisplaySwitchAreaViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun ExpenditureItemList(
    navController: NavController,
    viewModel: DisplaySwitchAreaViewModel = hiltViewModel()
) {

    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setStatusBarColor(color = Color(0xFF854A2A))
    }

    val df = SimpleDateFormat("yyyy-MM-dd", Locale.JAPANESE)

    Log.d(
        "支出項目 支出一覧、日付出力範囲",
        "${viewModel.startDate()} - ${viewModel.lastDate()}"
    )
    val listItem by viewModel.categorizeExpenditureItem(
        startDate = df.format(viewModel.startDate()),
        lastDate = df.format(viewModel.lastDate())
    ).collectAsState(initial = emptyList())

    // 金額合計
    var totalTax = 0
    listItem.forEach {
        totalTax += it.price.toInt()
    }

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
                            },
                            content = {
                                Icon(
                                    imageVector = Icons.Default.Menu,
                                    contentDescription = "メニュー",
                                    tint = Color(0xFF854A2A)
                                )
                            }
                        )
                    },
                    actions = {
                        IconButton(
                            onClick = {
                                var startDate =
                                    df.format(viewModel.standardOfStartDate)
                                var lastDate =
                                    df.format(viewModel.standardOfStartDate)
                                if (viewModel.dateProperty == DateProperty.CUSTOM.name) {
                                    startDate =
                                        df.format(viewModel.customOfStartDate)
                                    lastDate =
                                        df.format(viewModel.customOfLastDate)
                                }
                                navController.navigate(
                                    "${Route.PAY_DETAIL.name}/0/${viewModel.dateProperty}/${startDate}/${lastDate}"
                                )
                            },
                            content = {
                                Icon(
                                    imageVector = Icons.Default.ReceiptLong,
                                    contentDescription = "詳細",
                                    tint = Color(0xFF854A2A)
                                )
                            }
                        )
                    }
                )
            },
            floatingActionButton = { FAButton(onClick = { navController.navigate(Route.EDIT_EXPENDITURE.name) }) }
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .background(color = Color(0xFFEEDCB3))
                    .fillMaxSize(),
                content = {
                    DisplaySwitchArea(
                        totalTax = totalTax,
                        viewModel = viewModel
                    )
                    LazyColumn(
                        modifier = Modifier
                            .padding(top = 32.dp)
                            .padding(bottom = 80.dp),
                        content = {
                            items(listItem) {
                                Item(
                                    item = it,
                                    navController = navController,
                                    viewModel = viewModel
                                )
                            }
                        }
                    )
                }
            )
        }
    }
}

@Composable
private fun Item(
    item: CategorizeExpenditureItem,
    navController: NavController,
    viewModel: DisplaySwitchAreaViewModel
) {
    Column(
        modifier = Modifier
            .padding(bottom = 16.dp)
            .padding(horizontal = 8.dp)
            .clickable {
                val df = SimpleDateFormat("yyyy-MM-dd", Locale.JAPANESE)
                var startDate = df.format(viewModel.standardOfStartDate)
                var lastDate = df.format(viewModel.standardOfStartDate)
                if (viewModel.dateProperty == DateProperty.CUSTOM.name) {
                    startDate = df.format(viewModel.customOfStartDate)
                    lastDate = df.format(viewModel.customOfLastDate)
                }
                navController.navigate(
                    "${Route.PAY_DETAIL.name}/${item.id}/${viewModel.dateProperty}/${startDate}/${lastDate}"
                )
            }
            .clip(RoundedCornerShape(5.dp))
            .background(Color.White),
        content = {
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 4.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                content = {
                    Text(text = item.categoryName, fontSize = 20.sp)
                    Column(
                        horizontalAlignment = Alignment.End,
                        content = {
                            Text(text = "￥${item.price}", fontSize = 20.sp)
                            Text(
                                text = "支出回数：${item.categoryId}回",
                                fontSize = 14.sp,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }
                    )
                }
            )
        }
    )
}

@Composable
fun Drawer(
    navController: NavController,
    drawerState: DrawerState,
    scope: CoroutineScope
) {
    ModalDrawerSheet(
        modifier = Modifier.width(256.dp),
        drawerShape = MaterialTheme.shapes.extraSmall,
        content = {
            Column(
                modifier = Modifier
                    .background(color = Color(0xFF854A2A))
                    .fillMaxWidth()
                    .height(64.dp),
                verticalArrangement = Arrangement.Center,
                content = {
                    IconButton(
                        onClick = {
                            scope.launch {
                                drawerState.apply {
                                    if (isClosed) open() else close()
                                }
                            }
                        },
                        content = {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "閉じる",
                                tint = Color(0xFFEEDCB3)
                            )
                        }
                    )
                }
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF854A2A)),
                content = {
                    TextButton(
                        onClick = {
                            scope.launch {
                                drawerState.apply { close() }
                            }
                            navController.navigate(Route.CATEGORY_SETTING.name)
                        },
                        modifier = Modifier.padding(top = 16.dp),
                        content = {
                            Text(
                                text = "カテゴリ設定",
                                fontSize = 16.sp,
                                color = Color(0xFFEEDCB3),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    )
                }
            )
        }
    )
}