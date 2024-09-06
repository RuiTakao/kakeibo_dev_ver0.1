package com.example.kakeibo_dev_6.component

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.kakeibo_dev_6.component.page.EditCategory
import com.example.kakeibo_dev_6.component.page.EditExpenditureItem
import com.example.kakeibo_dev_6.component.page.ExpenditureDetail
import com.example.kakeibo_dev_6.component.page.ExpenditureItemDetail
import com.example.kakeibo_dev_6.component.page.ExpenditureItemList
import com.example.kakeibo_dev_6.component.page.SettingCategory
import com.example.kakeibo_dev_6.enum.Route
import com.example.kakeibo_dev_6.ui.theme.Kakeibo_dev_6Theme

@Composable
fun Navigation() {
    val navController = rememberNavController()

    Kakeibo_dev_6Theme {
        NavHost(
            navController = navController,
            startDestination = Route.EXPENDITURE_ITEM_LIST.name
        ) {

            // 支出項目ページ
            composable(route = Route.EXPENDITURE_ITEM_LIST.name) {
                ExpenditureItemList(navController = navController)
            }

            // 支出項目　明細ページ
            composable(
                route = "${Route.PAY_DETAIL.name}/{categoryId}/{dateProperty}/{startDate}/{lastDate}",
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

            // 支出項目　詳細
            composable(
                route = "${Route.EXPENDITURE_ITEM_DETAIL.name}/{id}",
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) { backStackEntry ->
                ExpenditureItemDetail(
                    navController = navController,
                    id = backStackEntry.arguments?.getInt("id")
                )
            }

            // カテゴリー設定
            composable(route = Route.CATEGORY_SETTING.name) {
                SettingCategory(navController = navController)
            }

            // カテゴリー編集
            composable(
                route = "${Route.EDIT_CATEGORY.name}/{id}",
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) { backStackEntry ->
                EditCategory(
                    navController = navController,
                    id = backStackEntry.arguments?.getInt("id")
                )
            }

            // カテゴリー登録
            composable(route = Route.EDIT_CATEGORY.name) {
                EditCategory(navController = navController)
            }

            // 支出項目編集
            composable(
                route = "${Route.EDIT_EXPENDITURE.name}/{id}",
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) { backStackEntry ->
                EditExpenditureItem(
                    navController = navController,
                    id = backStackEntry.arguments?.getInt("id")
                )
            }

            // 支出項目登録
            composable(route = Route.EDIT_EXPENDITURE.name) {
                EditExpenditureItem(navController = navController)
            }
        }
    }
}