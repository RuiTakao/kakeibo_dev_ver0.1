package com.example.kakeibo_dev_6.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material3.TextButton
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.kakeibo_dev_6.GroupCategory
import com.example.kakeibo_dev_6.MainViewModel
import com.example.kakeibo_dev_6.route.Route
import com.example.kakeibo_dev_6.weekLastDate
import com.example.kakeibo_dev_6.weekStartDate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.TemporalAdjusters
import java.util.Calendar
import java.util.Date

@Composable
fun ExpenditureList(
    navController: NavController,
    viewModel: MainViewModel = hiltViewModel(),
    dateProperty: String? = null,
    startDate: String? = null,
    lastDate: String? = null
) {
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
            viewModel = viewModel,
            dateProperty = dateProperty?.let { dateProperty } ?: "",
            startDate = startDate?.let { startDate } ?: "",
            lastDate = lastDate?.let { lastDate } ?: ""
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
    viewModel: MainViewModel,
    dateProperty: String,
    startDate: String,
    lastDate: String
) {
    Scaffold(
        topBar = {
            TopBar(
                drawerState = drawerState,
                scope = scope,
                navController = navController
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate(Route.EDIT_EXPENDITURE.name) }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "追加")
            }
        }
    ) { padding ->
        if (viewModel.mainPageState) {
            Column(
                modifier = Modifier.padding(padding)
            ) {
                TextButton(onClick = { viewModel.mainPageState = true }) {
                    Text(text = "eeeeee")
                }
            }
        } else {
            body(
                navController = navController,
                dateProperty = dateProperty,
                startDate = startDate,
                lastDate = lastDate,
                padding = padding,
                viewModel = viewModel
            )
        }
    }
}

@Composable
private fun body(
    navController: NavController,
    dateProperty: String,
    startDate: String,
    lastDate: String,
    padding: PaddingValues,
    viewModel: MainViewModel
) {
    val df = SimpleDateFormat("yyyy-MM-dd")
    val EditExpendList by viewModel.groupeExpendItem(
        firstDay = df.format(viewModel.startDate),
        lastDay = df.format(viewModel.lastDate)
    ).collectAsState(initial = emptyList())
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

    Column(
        modifier = Modifier
            .padding(padding)
            .background(color = Color(0xFFF7F7F7))
            .fillMaxSize()
    ) {
        controlContent(
            totalTax = totalTax,
            dateProperty = dateProperty,
            startDate = startDate.toDate("yyyy-MM-dd"),
            lastDate = lastDate.toDate("yyyy-MM-dd"),
            navController = navController,
            viewModel = viewModel
        )
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
                            Text(
                                text = "支出回数：${expendItem.id}回",
                                fontSize = 14.sp,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun controlContent(
    totalTax: Int,
    dateProperty: String,
    startDate: Date?,
    lastDate: Date?,
    navController: NavController,
    viewModel: MainViewModel
) {
    Column(modifier = Modifier.background(color = MaterialTheme.colorScheme.onPrimary)) {
        controlSpanContent(dateProperty = dateProperty, navController = navController, viewModel = viewModel)
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowLeft,
                contentDescription = null
            )
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                when (viewModel.dateProperty) {
                    "day" -> {
                        val df = SimpleDateFormat("M月d日")
                        Text(
                            text = df.format(viewModel.startDate),
                            fontSize = 24.sp
                        )
                    }

                    "week" -> {
                        val df = SimpleDateFormat("M月d日")
                        Text(
                            text = "${df.format(viewModel.startDate)} 〜 ${df.format(viewModel.lastDate)}",
                            fontSize = 24.sp
                        )
                    }

                    "month" -> {
                        val df = SimpleDateFormat("M月")
                        Text(
                            text = startDate?.let { df.format(startDate) } ?: "",
                            fontSize = 24.sp
                        )
                    }

                    else -> {
                        val df = SimpleDateFormat("M月d日")
                        Text(
                            text = "${
                                startDate?.let { df.format(startDate) } ?: ""
                            } 〜 ${
                                lastDate?.let { df.format(lastDate) } ?: ""
                            }",
                            fontSize = 24.sp
                        )
                    }
                }
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
}

@Composable
private fun controlSpanContent(dateProperty: String, navController: NavController, viewModel: MainViewModel) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    ) {
        TextButton(
            onClick = {
                val def = SimpleDateFormat("yyyy-MM-dd")
                val date = def.format(Date())
//                navController.navigate(
//                    "${Route.EXPENDITURE_LIST.name}/day/${date}/${date}"
//                )
                viewModel.startDate = Date()
                viewModel.lastDate = Date()
                viewModel.dateProperty = "day"
            },
            content = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "日", fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(2.dp))
                    Spacer(
                        modifier = Modifier
                            .height(2.dp)
                            .width(20.dp)
                            .background(if (dateProperty == "day") MaterialTheme.colorScheme.primary else Color.Transparent)
                    )
                }
            })
        TextButton(onClick = {
            val def = SimpleDateFormat("yyyy-MM-dd")
            val calendar: Calendar = Calendar.getInstance()
            val firstDay = Calendar.getInstance()
            val lastDay = Calendar.getInstance()
            calendar.time = Date()
            firstDay.add(Calendar.DATE, (calendar.get(Calendar.DAY_OF_WEEK) - 1) * -1)
            lastDay.add(Calendar.DATE, 7 - calendar.get(Calendar.DAY_OF_WEEK))
            val firstDate = def.format(firstDay.time)
            val lastDate = def.format(lastDay.time)
//            navController.navigate(
//                "${Route.EXPENDITURE_LIST.name}/week/${firstDate}/${lastDate}"
//            )
            viewModel.startDate = weekStartDate()
            viewModel.lastDate = weekLastDate()
            viewModel.dateProperty = "week"
        }, content = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "週", fontSize = 16.sp)
                Spacer(modifier = Modifier.height(2.dp))
                Spacer(
                    modifier = Modifier
                        .height(2.dp)
                        .width(20.dp)
                        .background(if (dateProperty == "week") MaterialTheme.colorScheme.primary else Color.Transparent)
                )
            }
        })
        TextButton(
            onClick = {
                val date = LocalDate.now()
                val firstDate = date.with(TemporalAdjusters.firstDayOfMonth())
                val lastDate = date.with(TemporalAdjusters.lastDayOfMonth())
//                navController.navigate("${Route.EXPENDITURE_LIST.name}/month/${firstDate}/${lastDate}")
//                viewModel.mainPageState = false
                viewModel.startDate = Date.from(firstDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
                viewModel.lastDate = Date.from(lastDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
                viewModel.dateProperty = "month"
            },
            content = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "月", fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(2.dp))
                    Spacer(
                        modifier = Modifier
                            .height(2.dp)
                            .width(20.dp)
                            .background(if (dateProperty == "month") MaterialTheme.colorScheme.primary else Color.Transparent)
                    )
                }
            })
        IconButton(onClick = { viewModel.mainPageState = true }, content = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(imageVector = Icons.Default.DateRange, contentDescription = null)
                Spacer(modifier = Modifier.height(2.dp))
                Spacer(
                    modifier = Modifier
                        .height(2.dp)
                        .width(20.dp)
                        .background(Color.Transparent)
                )
            }
        })
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