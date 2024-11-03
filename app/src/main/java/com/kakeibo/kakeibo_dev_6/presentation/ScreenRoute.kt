package com.kakeibo.kakeibo_dev_6.presentation

sealed class ScreenRoute(val route: String) {

    /** 支出項目 */

    // 支出項目一覧ページ（カテゴリー毎）
    object ExpenditureItemList : ScreenRoute("expenditure_item_list")

    // 支出項目一覧ページ（明細）
    object ExpenditureStatement : ScreenRoute("expenditure_statement")

    // 支出項目　詳細
    object ExpenditureItemDetail : ScreenRoute("expenditure_item_detail")

    // 支出項目　追加
    object AddExpenditureItem : ScreenRoute("add_expenditure_item")

    // 支出項目　追加
    // 追加と一緒にカテゴリーの追加するときのルーティング
    object AddCategoryFromAddExpenditureItem : ScreenRoute("add_category_from_add_expenditure_item")

    // 支出項目　編集
    object EditExpenditureItem : ScreenRoute("edit_expenditure_item")

    // 支出項目　編集
    // 編集と一緒にカテゴリーの追加するときのルーティング
    object AddCategoryFromEditExpenditureItem : ScreenRoute("add_category_from_edit_expenditure_item")

    /** カテゴリー */

    // カテゴリー設定ページ
    object SettingCategory : ScreenRoute("setting_category")

    // カテゴリー追加
    object AddCategory : ScreenRoute("add_category")

    // カテゴリー編集
    object EditCategory : ScreenRoute("edit_category")

    // カテゴリー並替え
    object ReplaceOrderCategory : ScreenRoute("replace_order_category")
}