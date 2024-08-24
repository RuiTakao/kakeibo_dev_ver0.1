package com.example.kakeibo_dev_6.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
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
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.kakeibo_dev_6.GroupCategory
import com.example.kakeibo_dev_6.MainViewModel
import com.example.kakeibo_dev_6.route.Route
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ExpenditureList(navController: NavController, viewModel: MainViewModel = hiltViewModel()) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                navController = navController,
                drawerState = drawerState,
                scope = scope
            )
        }
    ) {
        MainContent(
            navController = navController,
            drawerState = drawerState,
            scope = scope,
            viewModel = viewModel
        )
    }
}

@Composable
private fun DrawerContent(
    navController: NavController,
    drawerState: DrawerState,
    scope: CoroutineScope
) {
    ModalDrawerSheet(modifier = Modifier.width(256.dp)) {
        Column(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.primary)
                .fillMaxWidth()
        ) {
            IconButton(onClick = {
                scope.launch {
                    drawerState.apply {
                        if (isClosed) open() else close()
                    }
                }
            }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "閉じる")
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.primary)
        ) {
            NavigationDrawerItem(
                label = { Text(text = "カテゴリ設定") },
                selected = true,
                onClick = { navController.navigate(Route.CATEGORY_SETTING.name) })
        }
    }
}

@Composable
private fun MainContent(
    navController: NavController,
    drawerState: DrawerState,
    scope: CoroutineScope,
    viewModel: MainViewModel
) {
//    val EditExpendList by viewModel.expendItem.collectAsState(initial = emptyList())
    val EditExpendList by viewModel.groupeExpendItem.collectAsState(initial = emptyList())
    var totalTax by remember {
        mutableStateOf(0)
    }
    LaunchedEffect(EditExpendList) {
        var i = 0
        EditExpendList.forEach {
            i += it.price.toInt()
        }
        totalTax = i
    }
    Scaffold(
        topBar = { TopBar(drawerState = drawerState, scope = scope, navController = navController) },
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
                .fillMaxSize()
        ) {
            Column(modifier = Modifier.background(color = MaterialTheme.colorScheme.onPrimary)) {
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    Text(text = "日", fontSize = 16.sp)
                    Text(text = "週", fontSize = 16.sp)
                    Text(text = "月", fontSize = 16.sp)
                    Icon(imageVector = Icons.Default.DateRange, contentDescription = null)
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp, horizontal = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowLeft,
                        contentDescription = null
                    )
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "7月14日 〜 7月20日", fontSize = 24.sp)
                        Text(
                            text = "使用額 ￥${totalTax}",
                            fontSize = 16.sp,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = null
                    )
                }

            }
//            ListTest(expendItem = EditExpendList)
            LazyColumn(modifier = Modifier.padding(top = 32.dp)) {
                items(EditExpendList) { expendItem ->
                    Column(
                        modifier = Modifier
                            .padding(bottom = 16.dp)
                            .background(Color.White)
                            .fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(horizontal = 16.dp, vertical = 4.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = expendItem.name, fontSize = 20.sp)
                            Column(horizontalAlignment = Alignment.End) {
                                Text(text = "￥${expendItem.price}", fontSize = 20.sp)
                                Text(text = "支出回数：${expendItem.id}回", fontSize = 14.sp, modifier = Modifier.padding(vertical = 4.dp))
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
private fun TopBar(drawerState: DrawerState, scope: CoroutineScope, navController: NavController) {
    TopAppBar(
        title = {
            Text(
                text = "支出項目",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        navigationIcon = {
            IconButton(
                onClick = {
                    scope.launch {
                        drawerState.apply {
                            if (isClosed) open() else close()
                        }
                    }
                }
            ) {
                Icon(imageVector = Icons.Default.Menu, contentDescription = "メニュー")
            }
        },
        actions = {
            IconButton(onClick = { navController.navigate(Route.PAY_DETAIL.name) }) {
                Icon(imageVector = Icons.Default.ExitToApp, contentDescription = "詳細")
            }
        }
    )
}

@Composable
private fun ListTest(expendItem: List<GroupCategory>) {
    LazyColumn(modifier = Modifier.padding(top = 32.dp)) {
        items(expendItem) { expendItem ->
            Column {
                Text(text = expendItem.name)
                Text(text = expendItem.price)
//                Text(text = expendItem.category_id)
                Text(text = expendItem.id.toString())
            }
        }
    }
}