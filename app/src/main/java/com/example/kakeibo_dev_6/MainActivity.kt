package com.example.kakeibo_dev_6

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
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