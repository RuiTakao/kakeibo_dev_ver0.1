package com.example.kakeibo_dev_6.component.page.settingCategory

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.kakeibo_dev_6.MainViewModel
import com.example.kakeibo_dev_6.component.page.settingCategory.parts.ListContent
import com.example.kakeibo_dev_6.component.page.settingCategory.parts.TopBar

@Composable
fun SettingCategory(navController: NavController, viewModel: MainViewModel = hiltViewModel()) {

    Scaffold(
        topBar = {
            TopBar(navController = navController)
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .background(Color(0xFFF7F7F7))
                .fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            ListContent(navController = navController, viewModel = viewModel)
        }
    }
}