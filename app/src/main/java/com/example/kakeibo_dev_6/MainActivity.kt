package com.example.kakeibo_dev_6

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.kakeibo_dev_6.component.Navigation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
//            setUp()
            Navigation()
        }
    }
}

@Composable
fun setUp(viewModel: MainViewModel = hiltViewModel()) {
    viewModel.content = "食費"
    viewModel.createExpendItem()
    viewModel.name = "食費"
    viewModel.order = 1
    viewModel.createCategory()
}

@Composable
fun MainContent(viewModel: MainViewModel = hiltViewModel()) {
    val expendItems by viewModel.expendItem.collectAsState(initial = emptyList())
    val categories by viewModel.category.collectAsState(initial = emptyList())
    LazyColumn() {
        items(expendItems) { expendItem ->
            Text(text = "支出: ${expendItem.content}")
        }
        items(categories) { category ->
            Text(text = "カテゴリー: ${category.name}")
        }
    }
}