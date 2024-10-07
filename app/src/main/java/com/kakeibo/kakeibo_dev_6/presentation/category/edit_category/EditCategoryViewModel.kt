package com.kakeibo.kakeibo_dev_6.presentation.category.edit_category

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kakeibo.kakeibo_dev_6.domain.model.Category
import com.kakeibo.kakeibo_dev_6.domain.model.ExpenditureItem
import com.kakeibo.kakeibo_dev_6.domain.repository.CategoryDao
import com.kakeibo.kakeibo_dev_6.domain.repository.ExpenditureItemDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditCategoryViewModel @Inject constructor(
    private val categoryDao: CategoryDao,
    private val expenditureItemDao: ExpenditureItemDao
) : ViewModel() {

    // 各プロパティ
    var name by mutableStateOf("")
    var order by mutableIntStateOf(0)

    // カテゴリーの並び順先頭を取得
    // カテゴリーの並び順番号、降順で表示する
    val maxOrderCategory = categoryDao.maxOrderCategory().distinctUntilChanged()

    // カテゴリー一覧
    // カテゴリー名重複確認に使用
    val categoryList = categoryDao.loadAllCategories().distinctUntilChanged()

    /**
     * idに紐付くカテゴリーが使用されている支出項目一覧
     * 使用中のカテゴリーは削除できないようにする為
     * 編集画面で使用
     *
     * @param categoryId Int カテゴリーID
     *
     * @return Flow<List<ExpenditureItem>>
     */
    fun categoryUsedInExpenditureItemList(categoryId: Int): Flow<List<ExpenditureItem>> {
        return expenditureItemDao.isUsedCategory(categoryId = categoryId)
    }

    /**
     * カテゴリー登録
     *
     * @return Unit
     */
    fun createCategory() {
        viewModelScope.launch {
            val newCategory = Category(categoryName = name, categoryOrder = order)
            categoryDao.insertCategory(newCategory)
        }
    }

    /**
     * idパラメーターから取得したカテゴリー
     *
     * @param id Int idパラメーター
     *
     * @return Flow<Category>
     */
    fun setEditingCategory(id: Int): Flow<Category> {
        return categoryDao.getOneOfCategory(id = id).distinctUntilChanged()
    }

    // カテゴリーの入力内容保持
    // 編集画面でのみ使用
    var editingCategory: Category? = null

    /**
     * カテゴリー更新
     *
     * @return Unit
     */
    fun updateCategory() {
        editingCategory?.let { category ->
            viewModelScope.launch {
                category.categoryName = name
                categoryDao.updateCategory(category)
            }
        }
    }

    /**
     * カテゴリー削除
     *
     * @return Unit
     */
    fun deleteCategory(category: Category) {
        viewModelScope.launch {
            categoryDao.deleteCategory(category)
        }
    }

    // カテゴリー名のバリデーションメッセージ
    var inputValidateCategoryText by mutableStateOf("")

    /**
     * バリデーション処理
     *
     * @param categoryName
     * @param categoryId
     * @param categoryList
     *
     * @return Boolean
     */
    fun validate(categoryName: String, categoryId: Int, categoryList: List<Category>?): Boolean {

        when {

            // カテゴリー名未入力
            categoryName == "" -> {
                inputValidateCategoryText = "カテゴリーが未入力です。"
                return true
            }

            // カテゴリー名10文字以上
            categoryName.length > 10 -> {
                inputValidateCategoryText = "カテゴリーは10文字以内で入力してください。"
                return true
            }

            else -> {
                // カテゴリー名重複
                try {

                    // ループで重複しているカテゴリーが無いか調べる
                    categoryList!!.forEach {

                        // カテゴリー名一致
                        if (it.categoryName == categoryName) {

                            // カテゴリー名が一致したが一致したカテゴリーと判定したカテゴリーのIDが一致した場合はループをスキップ
                            // 編集画面でのみ判定
                            if (categoryId == it.id) {
                                return@forEach
                            }
                            inputValidateCategoryText = "カテゴリー名が重複しています。"
                            return true
                        }
                    }
                } catch (e: NullPointerException) {
                    // nullエラーが出た場合のバリデーション

                    inputValidateCategoryText = "重大なエラーが発生しました、開発者に問い合わせしてください。"
                    return true
                }

                inputValidateCategoryText = ""
                return false
            }
        }
    }
}