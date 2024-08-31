package com.example.kakeibo_dev_6.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import com.example.kakeibo_dev_6.GroupCategory
import com.example.kakeibo_dev_6.MainViewModel
import com.example.kakeibo_dev_6.route.Route
import java.lang.IllegalArgumentException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun PayDetail(navController: NavController, viewModel: MainViewModel = hiltViewModel()) {
    val df = SimpleDateFormat("yyyy-MM-dd")
    val expList by viewModel.detailExpendItem(
        firstDay = df.format(viewModel.payDetailStartDate),
        lastDay = df.format(viewModel.payDetailLastDate)
    ).collectAsState(initial = emptyList())
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
                Box(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .background(color = Color(0xFFD9D9D9), shape = RoundedCornerShape(8))
                        .fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(8.dp)
                    ) {
                        val df = SimpleDateFormat("M月d日")
                        Text(
                            text = "表示期間： ${df.format(viewModel.payDetailStartDate)} 〜 ${
                                df.format(
                                    viewModel.payDetailLastDate
                                )
                            }", fontSize = 14.sp
                        )
                        Text(text = "カテゴリー： すべて", fontSize = 14.sp)
                        Text(text = "表示順： 日付降順", fontSize = 14.sp)
                    }
                    IconButton(
                        modifier = Modifier.align(Alignment.TopEnd),
                        onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Default.MoreVert, contentDescription = "検索")
                    }
                }
            }
            LazyColumn(modifier = Modifier.padding(top = 16.dp)) {
                items(expList) { expItem ->
                    if (expList.indexOf(expItem) == 0) {
                        List(expItem = expItem, titleFlag = true)
                    } else {
                        if (
                            expList.get(expList.indexOf(expItem)).payDate !=
                            expList.get(expList.indexOf(expItem) - 1).payDate
                        ) {
                            List(expItem = expItem, titleFlag = true)
                        } else {
                            List(expItem = expItem)
                        }
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
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "戻る")
            }
        }
    )
}

@Composable
private fun List(expItem: GroupCategory, titleFlag: Boolean = false) {
    if (titleFlag) {
        Text(
            text = SimpleDateFormat("M月d日").format(expItem.payDate.toDate("yyyy-MM-dd")),
            fontSize = 20.sp,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(bottom = 4.dp)
                .padding(top = 16.dp)
        )
    } else {
        Spacer(
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth()
                .background(Color.Gray)
        )
    }
    Column(
        modifier = Modifier
            .clickable {  }
            .background(Color.White)
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .fillMaxWidth()

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column {
                Text(text = expItem.content, fontSize = 20.sp, lineHeight = 0.sp)
                Text(
                    text = expItem.name,
                    modifier = Modifier.padding(vertical = 4.dp),
                    fontSize = 14.sp,
                    lineHeight = 0.sp
                )
            }
            Text(text = "￥${expItem.price}", fontSize = 20.sp)
        }
    }
}

fun String.toDate(pattern: String = "yyyy-MM-dd HH:mm:ss"): Date? {
    val format = try {
        SimpleDateFormat(pattern)
    } catch (e: IllegalArgumentException) {
        null
    }
    val date = format?.let {
        try {
            it.parse(this)
        } catch (e: ParseException) {
            null
        }
    }
    return date
}