package com.example.kakeibo_dev_6.presentation.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.kakeibo_dev_6.presentation.component.page.EditCategory
import com.example.kakeibo_dev_6.presentation.component.page.EditExpenditureItem
import com.example.kakeibo_dev_6.presentation.component.page.ExpenditureDetail
import com.example.kakeibo_dev_6.presentation.component.page.ExpenditureItemDetail
import com.example.kakeibo_dev_6.presentation.component.page.ExpenditureItemList
import com.example.kakeibo_dev_6.presentation.component.page.ReplaceOrderCategory
import com.example.kakeibo_dev_6.presentation.component.page.SettingCategory
import com.example.kakeibo_dev_6.presentation.ScreenRoute
import com.example.kakeibo_dev_6.presentation.theme.Kakeibo_dev_6Theme
import com.example.kakeibo_dev_6.presentation.viewModel.EditExpenditureItemViewModel
import com.example.kakeibo_dev_6.presentation.viewModel.SettingCategoryViewModel

@Composable
fun Navigation() {
    val navController = rememberNavController()

    Kakeibo_dev_6Theme {
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
                SettingCategory(navController = navController)
            }

            /**
             * カテゴリー編集
             */
            composable(
                route = ScreenRoute.EditCategory.route + "/{id}",
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) { backStackEntry ->
                EditCategory(
                    navController = navController,
                    id = backStackEntry.arguments?.getInt("id")
                )
            }

            /**
             * カテゴリー追加
             */
            composable(route = ScreenRoute.AddCategoryFromSettingCategory.route) {
                EditCategory(navController = navController)
            }

            /**
             * カテゴリー並替え
             */
            composable(route = ScreenRoute.ReplaceOrderCategory.route) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(ScreenRoute.SettingCategory.route)
                }
                val viewModel: SettingCategoryViewModel =
                    viewModel(viewModelStoreOwner = parentEntry)
                ReplaceOrderCategory(navController = navController, viewModel = viewModel)
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
                ExpenditureItemDetail(
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
                EditExpenditureItem(navController = navController)
            }

            /**
             * 支出項目　編集
             * 編集と一緒にカテゴリーの追加するときのルーティング
             */
            composable(
                route = ScreenRoute.AddCategoryFromEditExpenditureItem.route + "/{id}",
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(
                        ScreenRoute.EditExpenditureItem.route +
                                "/" +
                                backStackEntry.arguments?.getInt("id")
                    )
                }

                val viewModel: EditExpenditureItemViewModel =
                    viewModel(viewModelStoreOwner = parentEntry)
                EditCategory(
                    navController = navController,
                    editExpenditureItemViewModel = viewModel
                )
            }

            /**
             * 支出項目　追加
             * 追加と一緒にカテゴリーの追加するときのルーティング
             */
            composable(route = ScreenRoute.AddCategoryFromAddExpenditureItem.route) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(ScreenRoute.AddExpenditureItem.route)
                }

                val viewModel: EditExpenditureItemViewModel =
                    viewModel(viewModelStoreOwner = parentEntry)
                EditCategory(
                    navController = navController,
                    editExpenditureItemViewModel = viewModel
                )
            }
        }
    }
}