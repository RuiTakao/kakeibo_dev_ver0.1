package com.example.kakeibo_dev_6.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.kakeibo_dev_6.MainViewModel
import com.example.kakeibo_dev_6.route.Route

@Composable
fun PayDetail(navController: NavController, viewModel: MainViewModel = hiltViewModel()) {
    val expList by viewModel.detailExpendItem.collectAsState(initial = emptyList())
    var totalTax by remember {
        mutableStateOf(0)
    }
    LaunchedEffect(expList) {
        var i = 0
        expList.forEach {
            i += it.price.toInt()
        }
        totalTax = i
    }

    Scaffold(
        topBar = {
            TopBar(navController = navController)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .background(color = Color(0xFFF7F7F7))
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 32.dp)
            ) {
                Text(text = "￥${totalTax}", fontSize = 24.sp)
                Column(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .background(color = Color(0xFFD9D9D9), shape = RoundedCornerShape(8))
                        .fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text(text = "表示期間： 7月14日 〜 7月20日", fontSize = 14.sp)
                        Text(text = "カテゴリー： すべて", fontSize = 14.sp)
                        Text(text = "表示順： 日付降順", fontSize = 14.sp)
                    }
                }
            }
            LazyColumn(modifier = Modifier.padding(top = 16.dp)) {
                items(expList) { expItem ->
                    Text(
                        text = expItem.price,
                        fontSize = 20.sp,
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .padding(bottom = 4.dp)
                    )
                    Column(
                        modifier = Modifier
//                            .padding(bottom = 16.dp)
                            .background(Color.White)
                            .fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(start = 16.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = expItem.content, fontSize = 20.sp)
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ){
                                Text(text = "￥${expItem.price}", fontSize = 20.sp)
                                IconButton(onClick = { /*TODO*/ }) {
                                    Icon(imageVector = Icons.Default.MoreVert, contentDescription = "支出編集", modifier = Modifier.size(20.dp))
                                }
                            }
                        }
                        Text(text = expItem.name, fontSize = 14.sp, modifier = Modifier.padding(start = 16.dp), lineHeight = 1.sp)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(navController: NavController) {
    TopAppBar(
        title = {
            Text(
                text = "支出項目 明細",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        navigationIcon = {
            IconButton(onClick = { navController.navigate(Route.EXPENDITURE_LIST.name) }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "戻る")
            }
        }
    )
}