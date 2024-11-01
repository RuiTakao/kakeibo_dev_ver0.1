package com.kakeibo.kakeibo_dev_6.presentation.expenditure_item.expenditure_item_list

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kakeibo.kakeibo_dev_6.presentation.expenditure_item.expenditure_item_list.component.ExpenditureItemList
import com.kakeibo.kakeibo_dev_6.presentation.expenditure_item.expenditure_item_list.component.ExpenditureItemListTemplate
import com.kakeibo.kakeibo_dev_6.presentation.expenditure_item.expenditure_item_list.component.ExpenditureStatement

@Composable
fun ExpenditureItemListScreen(
    navController: NavController,
    viewModel: ExpenditureItemListViewModel = hiltViewModel()
) {
    ExpenditureItemListTemplate(navController = navController) {

        if (viewModel.switchArea) {
            ExpenditureItemList(navController = navController, viewModel = viewModel)
        } else {
            ExpenditureStatement(navController = navController, viewModel = viewModel)
        }
    }
}

