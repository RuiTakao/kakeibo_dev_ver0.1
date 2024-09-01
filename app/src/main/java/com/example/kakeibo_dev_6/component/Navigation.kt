package com.example.kakeibo_dev_6.component

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.kakeibo_dev_6.component.page.expenditureDetail.ExpenditureDetail
import com.example.kakeibo_dev_6.component.page.expenditureItemList.ExpenditureItemList
import com.example.kakeibo_dev_6.route.Route
import com.example.kakeibo_dev_6.ui.theme.Kakeibo_dev_6Theme

@Composable
fun Navigation() {
    val navController = rememberNavController()

    Kakeibo_dev_6Theme {
        NavHost(
            navController = navController,
            startDestination = Route.EXPENDITURE_ITEM_LIST.name
        ) {
            composable(route = Route.EXPENDITURE_ITEM_LIST.name) {
                ExpenditureItemList(navController = navController)
            }
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




            composable(route = Route.EDIT_EXPENDITURE.name) {
                EditExpendItem(navController = navController)
            }
            composable(route = Route.CATEGORY_SETTING.name) {
                SettingCategory(navController = navController)
            }
            composable(
                route = "${Route.EDIT_CATEGORY.name}/{id}",
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) { backStackEntry ->
                EditCategory(
                    navController = navController,
                    id = backStackEntry.arguments?.getInt("id")
                )
            }
            composable(route = Route.EDIT_CATEGORY.name) {
                EditCategory(navController = navController)
            }

        }
    }
}