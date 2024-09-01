package com.example.kakeibo_dev_6.component

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.kakeibo_dev_6.component.page.editCategory.EditCategory
import com.example.kakeibo_dev_6.component.page.editExpenditureItem.EditExpenditureItem
import com.example.kakeibo_dev_6.component.page.expenditureDetail.ExpenditureDetail
import com.example.kakeibo_dev_6.component.page.expenditureItemDetail.ExpenditureItemDetail
import com.example.kakeibo_dev_6.component.page.expenditureItemList.ExpenditureItemList
import com.example.kakeibo_dev_6.component.page.settingCategory.SettingCategory
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
                route = "${Route.PAY_DETAIL.name}/{categoryId}/{startDate}/{lastDate}",
                arguments = listOf(
                    navArgument("categoryId") { type = NavType.StringType },
                    navArgument("startDate") { type = NavType.StringType },
                    navArgument("lastDate") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                ExpenditureDetail(
                    navController = navController,
                    categoryId = backStackEntry.arguments?.getString("categoryId"),
                    startDate = backStackEntry.arguments?.getString("startDate"),
                    lastDate = backStackEntry.arguments?.getString("lastDate")
                )
            }

            // 支出項目　詳細
            composable(
                route = "${Route.EXPENDITURE_ITEM_DETAIL.name}/{id}",
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) {backStackEntry ->
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






            composable(route = Route.EDIT_EXPENDITURE.name) {
                EditExpenditureItem(navController = navController)
            }
        }
    }
}