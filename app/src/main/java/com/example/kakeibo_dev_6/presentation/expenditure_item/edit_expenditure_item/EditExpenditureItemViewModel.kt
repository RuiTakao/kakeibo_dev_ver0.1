package com.example.kakeibo_dev_6.presentation.expenditure_item.edit_expenditure_item

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kakeibo_dev_6.domain.model.ExpenditureItem
import com.example.kakeibo_dev_6.domain.repository.CategoryDao
import com.example.kakeibo_dev_6.domain.repository.ExpenditureItemDao
import com.example.kakeibo_dev_6.common.utility.checkInt
import com.example.kakeibo_dev_6.common.utility.toDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditExpenditureItemViewModel @Inject constructor(
    private val expenditureItemDao: ExpenditureItemDao,
    categoryDao: CategoryDao
) : ViewModel() {

    // 各プロパティ
    var payDate by mutableStateOf("")
    var categoryId by mutableStateOf("")
    var content by mutableStateOf("")
    var price by mutableStateOf("")

    /**
     * 支出項目登録
     *
     * @return Unit
     */
    fun createExpendItem() {
        viewModelScope.launch {
            val newExpendItem = ExpenditureItem(
                payDate = payDate,
                categoryId = categoryId,
                content = content,
                price = price
            )
            expenditureItemDao.insertExpenditureItem(newExpendItem)
        }
    }

    /**
     * idパラメーターから取得した支出項目
     *
     * @param id Int dパラメーター
     *
     * @return Flow<ExpenditureItem>
     */
    fun setEditingExpendItem(id: Int): Flow<ExpenditureItem> {
        return expenditureItemDao.loadExpenditureItem(id = id).distinctUntilChanged()
    }

    // 支出項目の入力内容保持
    // 編集画面でのみ使用
    var editingExpendItem: ExpenditureItem? = null

    /**
     * 支出項目更新
     *
     * @return Unit
     */
    fun updateExpendItem() {
        editingExpendItem?.let {
            viewModelScope.launch {
                it.payDate = payDate
                it.price = price
                it.categoryId = categoryId
                it.content = content
                expenditureItemDao.updateExpenditureItem(it)
            }
        }
    }

    // 日付のバリデーションメッセージ
    var inputValidatePayDateText by mutableStateOf("")
    // 金額のバリデーションメッセージ
    var inputValidatePriceText by mutableStateOf("")
    // カテゴリーのバリデーションメッセージ
    var inputValidateSelectCategoryText by mutableStateOf("")
    // 内容のバリデーションメッセージ
    var inputValidateContentText by mutableStateOf("")

    // カテゴリー全件取得
    // セレクトボックスにカテゴリー全件表示する
    val category = categoryDao.loadAllCategories().distinctUntilChanged()

    /**
     * バリデーション処理
     *
     * @param payDate String
     * @param price String
     * @param categoryId String
     * @param content String
     *
     * @return Boolean
     */
    fun validate(payDate: String, price: String, categoryId: String, content: String): Boolean {

        // バリデーションエラー数をカウントするフラグ
        var validCount = 0

        // 日付のバリデーション
        inputValidatePayDateText =
            if (payDate.toDate("yyyy-MM-dd") == null) {
                validCount++
                "日付を入力してください。"
            } else ""

        // 金額のバリデーション
        inputValidatePriceText =
            if (price == "") {
                validCount++
                "金額を入力してください。"
            } else if (!checkInt(price)) {
                validCount++
                "金額が不正です。"
            } else ""

        // カテゴリーのバリデーション
        inputValidateSelectCategoryText =
            if (categoryId == "") {
                validCount++
                "カテゴリーが未選択です。"
            } else ""

        // 内容のバリデーション
        inputValidateContentText =
            if (content == "") {
                validCount++
                "内容が未入力です。"
            } else if (content.length > 50) {
                validCount++
                "内容は50文字以内で入力してください。"
            } else ""

        // バリデーションカウント数が0であればバリデーションエラー無し
        // バリデーションカウント数が1以上であればバリデーションエラーあり
        return validCount == 0
    }

    // 支出項目登録、編集中にカテゴリーの追加があったか判定
    var addCategoryFlg by mutableStateOf(false)
    // 支出項目登録、編集中に追加したカテゴリーの並び順
    var addCategoryOrder by mutableIntStateOf(0)
}