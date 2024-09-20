package com.example.kakeibo_dev_6.presentation

sealed class ScreenRoute(val route: String) {
    object CategorizeExpenditureItemList : ScreenRoute("categorize_expenditure_item_list")
    object ExpenditureItemList : ScreenRoute("expenditure_item_list")
    object ExpenditureItemDetail : ScreenRoute("expenditure_item_detail")
    object AddExpenditureItem : ScreenRoute("add_expenditure_item")
    object EditExpenditureItem : ScreenRoute("edit_expenditure_item")
    object SettingCategory : ScreenRoute("setting_category")
    object AddCategoryFromSettingCategory : ScreenRoute("add_category_from_setting_category")
    object AddCategoryFromAddExpenditureItem : ScreenRoute("add_category_from_add_expenditure_item")
    object AddCategoryFromEditExpenditureItem : ScreenRoute("add_category_from_edit_expenditure_item")
    object EditCategory : ScreenRoute("edit_category")
    object ReplaceOrderCategory : ScreenRoute("replace_order_category")
}