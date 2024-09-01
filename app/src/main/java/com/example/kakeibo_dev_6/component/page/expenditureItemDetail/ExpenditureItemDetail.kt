package com.example.kakeibo_dev_6.component.page.expenditureItemDetail

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.kakeibo_dev_6.GroupCategory
import com.example.kakeibo_dev_6.MainViewModel
import java.lang.IllegalArgumentException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun ExpenditureItemDetail(
    navController: NavController,
    id: Int?,
    viewModel: MainViewModel = hiltViewModel()
) {
    id?.let {

        val expenditureItem by viewModel.setEditingExpendItem(id = id).collectAsState(initial = null)
        val categories by viewModel.category.collectAsState(initial = emptyList())

        val id = remember { mutableStateOf(0) }
        val payDate = remember { mutableStateOf("") }
        val price = remember { mutableStateOf("") }
        val category_id = remember { mutableStateOf("") }
        val content = remember { mutableStateOf("") }

        LaunchedEffect(expenditureItem) {
            expenditureItem?.let {
                val yMd = SimpleDateFormat("y年M月d日")

                Log.d("expenditureItem", expenditureItem.toString())

                id.value = expenditureItem!!.id
                payDate.value = yMd.format(expenditureItem!!.payDate.toDate("yyyy-MM-dd"))
                price.value = expenditureItem!!.price
                category_id.value = expenditureItem!!.category_id
                content.value = expenditureItem!!.content
                viewModel.editingExpendItem = expenditureItem
            }
        }

        Scaffold(topBar = {
            TopBar(navController = navController, onClick = {

            })
        }) { padding ->

            Column(modifier = Modifier.padding(padding)) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "日付",
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Text(
                        text = payDate.value,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "金額",
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Text(text = "￥${price.value}", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "カテゴリー",
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    categories.forEach{
                        if (it.id.toString() == category_id.value) {
                            Text(text = it.name, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "内容",
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Text(text = content.value, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(48.dp))
                IconButton(
                    onClick = {
                        viewModel.deleteExpendItem(viewModel.editingExpendItem!!)
                        navController.popBackStack()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "削除",
                        tint = Color.Red
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(navController: NavController, onClick: () -> Unit) {
    TopAppBar(
        title = {
            Text(
                text = "支出項目 詳細",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            IconButton(onClick = {
                navController.popBackStack()
            }) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "閉じる")
            }
        },
        actions = {
            IconButton(
                onClick = onClick
            ) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = "編集")
            }
        }
    )
}

private fun String.toDate(pattern: String = "yyyy-MM-dd HH:mm:ss"): Date? {
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