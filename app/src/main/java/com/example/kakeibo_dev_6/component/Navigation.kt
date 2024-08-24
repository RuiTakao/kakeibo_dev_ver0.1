package com.example.kakeibo_dev_6.component

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.kakeibo_dev_6.route.Route
import com.example.kakeibo_dev_6.ui.theme.Kakeibo_dev_6Theme

@Composable
fun Navigation() {
    val navController = rememberNavController()
    Kakeibo_dev_6Theme {
        NavHost(navController = navController, startDestination = Route.EXPENDITURE_LIST.name) {
            composable(route = Route.EXPENDITURE_LIST.name) {
                ExpenditureList(navController = navController)
            }
            composable(route = Route.EDIT_EXPENDITURE.name) {
                EditExpendItem(navController = navController)
            }
            composable(route = Route.CATEGORY_SETTING.name) {
                SettingCategory(navController = navController)
            }
            composable(
                route = "${Route.EDIT_CATEGORY.name}/{id}",
                arguments = listOf(navArgument("id", ) { type = NavType.IntType })
            ) { backStackEntry ->
                EditCategory(navController = navController, id = backStackEntry.arguments?.getInt("id"))
            }
            composable(route = Route.EDIT_CATEGORY.name) {
                EditCategory(navController = navController)
            }
        }
    }
}

@Composable
fun DD(navController: NavController) {
    Column {
        Text(text = "hello")
    }
}