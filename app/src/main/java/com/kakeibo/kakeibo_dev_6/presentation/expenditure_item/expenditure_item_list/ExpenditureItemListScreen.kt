package com.kakeibo.kakeibo_dev_6.presentation.expenditure_item.expenditure_item_list

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kakeibo.kakeibo_dev_6.presentation.expenditure_item.expenditure_item_list.component.ExpenditureItemList
import com.kakeibo.kakeibo_dev_6.presentation.expenditure_item.expenditure_item_list.component.ExpenditureItemListTemplate

@Composable
fun ExpenditureItemListScreen(
    navController: NavController,
    viewModel: ExpenditureItemListViewModel = hiltViewModel()
) {
    ExpenditureItemListTemplate(navController = navController) {

        ExpenditureItemList(navController = navController, viewModel = viewModel)
    }
}

