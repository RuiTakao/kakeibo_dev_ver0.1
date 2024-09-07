package com.example.kakeibo_dev_6

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.app.AppLaunchChecker
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.kakeibo_dev_6.component.Navigation
import com.example.kakeibo_dev_6.viewModel.EditCategoryViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            if (!AppLaunchChecker.hasStartedFromLauncher(applicationContext)) {
                // 初回起動の場合実行
                val viewModel: EditCategoryViewModel = hiltViewModel()
                defaultCategory(viewModel)
            }

            // アプリの起動ステータス監視
            AppLaunchChecker.onActivityCreate(this)

            Navigation()
        }
    }
}

private fun defaultCategory(viewModel: EditCategoryViewModel) {

    viewModel.name = "🏠 生活費"
    viewModel.order = 1
    viewModel.createCategory()

    viewModel.name = "🛍 日用品費"
    viewModel.order = 2
    viewModel.createCategory()

    viewModel.name = "🎾 娯楽費"
    viewModel.order = 3
    viewModel.createCategory()

    viewModel.name = "🍙 食費"
    viewModel.order = 4
    viewModel.createCategory()

    viewModel.name = "👪 交際費"
    viewModel.order = 5
    viewModel.createCategory()

    viewModel.name = "📺 家具・家電"
    viewModel.order = 6
    viewModel.createCategory()

    viewModel.name = "❓ その他"
    viewModel.order = 7
    viewModel.createCategory()
}