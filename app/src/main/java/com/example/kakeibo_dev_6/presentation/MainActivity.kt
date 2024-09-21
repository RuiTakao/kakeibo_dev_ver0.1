package com.example.kakeibo_dev_6.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.app.AppLaunchChecker
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.kakeibo_dev_6.presentation.category.add_category.AddCategoryScreen
import com.example.kakeibo_dev_6.presentation.category.edit_category.EditCategoryScreen
import com.example.kakeibo_dev_6.presentation.category.replace_order_category.ReplaceOrderCategoryScreen
import com.example.kakeibo_dev_6.presentation.category.setting_category.SettingCategoryScreen
import com.example.kakeibo_dev_6.presentation.component.page.EditCategory
import com.example.kakeibo_dev_6.presentation.component.page.EditExpenditureItem
import com.example.kakeibo_dev_6.presentation.component.page.ExpenditureDetail
import com.example.kakeibo_dev_6.presentation.component.page.ExpenditureItemList
import com.example.kakeibo_dev_6.presentation.expenditure_item.add_expenditure_item.AddExpenditureItemScreen
import com.example.kakeibo_dev_6.presentation.expenditure_item.add_expenditure_item.add_category_from_add_expenditure_item.AddCategoryFromAddExpenditureItemScreen
import com.example.kakeibo_dev_6.presentation.expenditure_item.expenditure_item_detail.ExpenditureItemDetailScreen
import com.example.kakeibo_dev_6.presentation.ui.theme.Kakeibo_dev_6Theme
import com.example.kakeibo_dev_6.presentation.viewModel.EditCategoryViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
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
            Kakeibo_dev_6Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFFEEDCB3)
                ) {
                    // ステータスバーの色
                    val systemUiController = rememberSystemUiController()

                    SideEffect {
                        systemUiController.setStatusBarColor(color = Color(0xFF854A2A))
                    }

                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        // 支出項目一覧ページ（カテゴリー毎）を最初に表示するページにする
                        startDestination = ScreenRoute.CategorizeExpenditureItemList.route
                    ) {

                        /** カテゴリー */
                        /**
                         * カテゴリー設定ページ
                         */
                        composable(route = ScreenRoute.SettingCategory.route) {
                            SettingCategoryScreen(navController = navController)
                        }

                        /**
                         * カテゴリー編集
                         */
                        composable(
                            route = ScreenRoute.EditCategory.route + "/{id}",
                            arguments = listOf(navArgument("id") { type = NavType.IntType })
                        ) { backStackEntry ->
                            EditCategoryScreen(
                                navController = navController,
                                id = backStackEntry.arguments?.getInt("id")
                            )
                        }

                        /**
                         * カテゴリー追加
                         */
                        composable(route = ScreenRoute.AddCategory.route) {
                            AddCategoryScreen(navController = navController)
                        }

                        /**
                         * カテゴリー並替え
                         */
                        composable(route = ScreenRoute.ReplaceOrderCategory.route) {
                            ReplaceOrderCategoryScreen(
                                navController = navController,
                                settingCategoryViewModel = viewModel(
                                    viewModelStoreOwner = remember(it) {
                                        navController.getBackStackEntry(ScreenRoute.SettingCategory.route)
                                    }
                                )
                            )
                        }

                        /** 支出項目 */
                        /**
                         * 支出項目一覧ページ（カテゴリー毎）
                         */
                        composable(route = ScreenRoute.CategorizeExpenditureItemList.route) {
                            ExpenditureItemList(navController = navController)
                        }

                        /**
                         * 支出項目一覧ページ（明細）
                         */
                        composable(
                            route = ScreenRoute.ExpenditureItemList.route + "/{categoryId}/{dateProperty}/{startDate}/{lastDate}",
                            arguments = listOf(
                                navArgument("categoryId") { type = NavType.StringType },
                                navArgument("dateProperty") { type = NavType.StringType },
                                navArgument("startDate") { type = NavType.StringType },
                                navArgument("lastDate") { type = NavType.StringType }
                            )
                        ) { backStackEntry ->
                            ExpenditureDetail(
                                navController = navController,
                                categoryId = backStackEntry.arguments?.getString("categoryId"),
                                dateProperty = backStackEntry.arguments?.getString("dateProperty"),
                                startDate = backStackEntry.arguments?.getString("startDate"),
                                lastDate = backStackEntry.arguments?.getString("lastDate")
                            )
                        }

                        /**
                         * 支出項目　詳細
                         */
                        composable(
                            route = ScreenRoute.ExpenditureItemDetail.route + "/{id}",
                            arguments = listOf(navArgument("id") { type = NavType.IntType })
                        ) { backStackEntry ->
                            ExpenditureItemDetailScreen(
                                navController = navController,
                                id = backStackEntry.arguments?.getInt("id")
                            )
                        }

                        /**
                         * 支出項目　編集
                         */
                        composable(
                            route = ScreenRoute.EditExpenditureItem.route + "/{id}",
                            arguments = listOf(navArgument("id") { type = NavType.IntType })
                        ) { backStackEntry ->
                            EditExpenditureItem(
                                navController = navController,
                                id = backStackEntry.arguments?.getInt("id")
                            )
                        }

                        /**
                         * 支出項目　追加
                         */
                        composable(route = ScreenRoute.AddExpenditureItem.route) {
                            AddExpenditureItemScreen(navController = navController)
                        }

                        /**
                         * 支出項目　編集
                         * 編集と一緒にカテゴリーの追加するときのルーティング
                         */
                        composable(
                            route = ScreenRoute.AddCategoryFromEditExpenditureItem.route + "/{id}",
                            arguments = listOf(navArgument("id") { type = NavType.IntType })
                        ) {
                            EditCategory(
                                navController = navController,
                                editExpenditureItemViewModel = viewModel(
                                    viewModelStoreOwner = remember(it) {
                                        navController.getBackStackEntry(
                                            ScreenRoute.EditExpenditureItem.route +
                                                    "/${it.arguments?.getInt("id")}"
                                        )
                                    }
                                )
                            )
                        }

                        /**
                         * 支出項目　追加
                         * 追加と一緒にカテゴリーの追加するときのルーティング
                         */
                        composable(route = ScreenRoute.AddCategoryFromAddExpenditureItem.route) {
                            AddCategoryFromAddExpenditureItemScreen(
                                navController = navController,
                                addExpenditureItemViewModel = viewModel(
                                    viewModelStoreOwner = remember(it) {
                                        navController.getBackStackEntry(ScreenRoute.AddExpenditureItem.route)
                                    }
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun defaultCategory(viewModel: EditCategoryViewModel) {

    viewModel.name = "❓ その他"
    viewModel.order = 7
    viewModel.createCategory()

    viewModel.name = "📺 家具・家電"
    viewModel.order = 6
    viewModel.createCategory()

    viewModel.name = "👪 交際費"
    viewModel.order = 5
    viewModel.createCategory()

    viewModel.name = "🍙 食費"
    viewModel.order = 4
    viewModel.createCategory()

    viewModel.name = "🎾 娯楽費"
    viewModel.order = 3
    viewModel.createCategory()

    viewModel.name = "🛍 日用品費"
    viewModel.order = 2
    viewModel.createCategory()

    viewModel.name = "🏠 生活費"
    viewModel.order = 1
    viewModel.createCategory()
}