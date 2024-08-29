package com.example.kakeibo_dev_6.component

import android.util.Log
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
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import java.util.Calendar
import java.util.Date

@Composable
fun Navigation() {
    val navController = rememberNavController()

    val def = SimpleDateFormat("yyyy-MM-dd")
    val calendar: Calendar = Calendar.getInstance()
    val firstDay = Calendar.getInstance()
    val lastDay = Calendar.getInstance()
    calendar.time = Date()
    firstDay.add(Calendar.DATE, (calendar.get(Calendar.DAY_OF_WEEK) - 1) * -1)
    lastDay.add(Calendar.DATE, 7 - calendar.get(Calendar.DAY_OF_WEEK))
    Kakeibo_dev_6Theme {
        NavHost(
            navController = navController,
            startDestination = "${Route.EXPENDITURE_LIST.name}/week/${
                def.format(firstDay.time)
            }/${
                def.format(lastDay.time)
            }"
        ) {
            composable(
                route = "${Route.EXPENDITURE_LIST.name}/{dateProperty}/{startDay}/{lastDay}",
                arguments = listOf(
                    navArgument("dateProperty") {
                        type = NavType.StringType
                        defaultValue = "week"
                    },
                    navArgument("startDay") {
                        type = NavType.StringType
                        defaultValue = def.format(firstDay.time)
                    },
                    navArgument("lastDay") {
                        type = NavType.StringType
                        defaultValue = def.format(lastDay.time)
                    }
                )
            ) { backStackEntry ->
                ExpenditureList(
                    navController = navController,
                    dateProperty = backStackEntry.arguments?.getString("dateProperty"),
                    startDate = backStackEntry.arguments?.getString("startDay"),
                    lastDate = backStackEntry.arguments?.getString("lastDay")
                )
            }
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
            composable(route = Route.PAY_DETAIL.name) {
                PayDetail(navController = navController)
            }
        }
    }
}