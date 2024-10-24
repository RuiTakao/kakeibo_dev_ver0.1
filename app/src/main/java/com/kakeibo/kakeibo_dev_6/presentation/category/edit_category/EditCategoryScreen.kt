package com.kakeibo.kakeibo_dev_6.presentation.category.edit_category

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kakeibo.kakeibo_dev_6.common.Colors
import com.kakeibo.kakeibo_dev_6.presentation.component.DeleteDialog
import com.kakeibo.kakeibo_dev_6.presentation.component.SubTopBar
import com.kakeibo.kakeibo_dev_6.presentation.expenditure_item.edit_expenditure_item.EditExpenditureItemViewModel

/**
 * カテゴリー追加画面
 * カテゴリー編集画面
 * 支出項目登録、編集画面から遷移して来た時のカテゴリー追加画面
 *
 * @param navController NavController
 * @param id Int?
 * @param editExpenditureItemViewModel EditExpenditureItemViewModel?
 * @param viewModel EditCategoryViewModel
 *
 * @return Unit
 */
@Composable
fun EditCategoryScreen(
    navController: NavController,
    id: Int? = null,
    editExpenditureItemViewModel: EditExpenditureItemViewModel? = null,
    viewModel: EditCategoryViewModel = hiltViewModel()
) {

    // バリデーション用、カテゴリーの重複が無いか確認
    val categoryList by viewModel.categoryList.collectAsState(initial = null)

    // カテゴリーの最前列取得 カテゴリーの並び順昇順で取得する
    val maxOrderCategory by viewModel.maxOrderCategory.collectAsState(initial = null)

    // フォーカスを当てる変数
    val focusRequester = remember { FocusRequester() }

    // フォーカスを末尾に配置する為の変数
    var textFieldValue by remember {
        mutableStateOf(
            TextFieldValue(
                text = "",
                selection = TextRange(0)
            )
        )
    }

    // idがnullかnotNullで追加画面、編集画面の判定をする
    if (id != null) {
        // 編集

        // idからカテゴリーを取得する
        val category by viewModel.setEditingCategory(id = id).collectAsState(initial = null)
        // カテゴリー読み込み後、各フィールドにデータ挿入する
        LaunchedEffect(category) {
            // nullエラー回避処理
            category?.let {

                // カテゴリー名
                // TextFieldValue型で扱う
                textFieldValue = TextFieldValue(
                    text = it.categoryName,
                    selection = TextRange(it.categoryName.length)
                )
                viewModel.name = textFieldValue.text

                // カテゴリーの入力内容保持
                viewModel.editingCategory = it
            }
        }
    }

    Scaffold(
        topBar = {
            SubTopBar(
                title = "カテゴリー" + if (id != null) "編集" else "追加",
                navigation = {
                    IconButton(
                        onClick = {

                            // 前の画面に戻る
                            navController.popBackStack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "閉じる",
                            tint = Color(0xFF854A2A)
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            // 登録処理

                            // バリデーション判定
                            if (!viewModel.validate(
                                    categoryName = viewModel.name,
                                    categoryId = id ?: 0,
                                    categoryList = categoryList
                                )
                            ) {
                                // バリデーションエラー無し

                                // 前の画面に戻る
                                navController.popBackStack()

                                // idがnullかnotNullで追加処理、編集処理の判定をする
                                if (id != null) {
                                    // 更新
                                    viewModel.updateCategory()
                                } else {
                                    // 登録

                                    // 登録済のカテゴリー順の番号に+1する
                                    viewModel.order =
                                        maxOrderCategory?.let { it.categoryOrder + 1 } ?: 0

                                    // 支出項目登録、編集からカテゴリー登録した場合の処理
                                    // 支出項目登録、編集のViewModelに登録するカテゴリーの順番の番号登録、登録完了フラグをオンにする
                                    editExpenditureItemViewModel?.let {
                                        it.addCategoryOrder = viewModel.order
                                        it.addCategoryFlg = true
                                    }

                                    // 登録処理
                                    viewModel.createCategory()
                                }
                            }
                        },
                        content = {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "登録",
                                tint = Color(0xFF854A2A)
                            )
                        }
                    )
                }
            )
        },
        containerColor = Color(Colors.BASE_COLOR),
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->

        Column(modifier = Modifier.padding(paddingValues)) {

            // カテゴリー名入力フィールド
            TextField(
                value = textFieldValue,
                onValueChange = {

                    // 入力テキストをViewModelに保存
                    // 文字数10文字まで
                    if (it.text.length <= 10) {
                        textFieldValue = it
                        viewModel.name = it.text
                    }
                },
                label = { Text(text = "カテゴリー名を入力してください") },
                singleLine = true, // 改行禁止
                modifier = Modifier
                    .padding(8.dp)
                    .padding(top = 16.dp)
                    .fillMaxWidth()
                    .focusRequester(focusRequester)
            )

            // フォーカスを当てる
            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }

            // バリデーションテキスト
            if (viewModel.inputValidateCategoryText != "") {
                Text(
                    text = viewModel.inputValidateCategoryText,
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            // 削除ボタン
            id?.let {

                // 編集中のカテゴリーが使用されている支出項目取得
                val categoryUsedInExpenditureItemList
                        by viewModel.categoryUsedInExpenditureItemList(categoryId = id)
                            .collectAsState(initial = null)

                // 編集中のカテゴリーが支出項目で使用されているか確認、使用されていた場合、削除不可
                val isCheckUsedCategory = categoryUsedInExpenditureItemList?.size ?: 0

                // その他のカテゴリーのIDか判定
                val isOtherCategory = id == 7

                // カテゴリーが削除可能か判定
                val isCheckUnDeletableCategory = isCheckUsedCategory > 0 || isOtherCategory

                // 削除ダイアログ表示非表示フラグ
                val isShowDialog = remember { mutableStateOf(false) }

                Row(
                    modifier = Modifier.padding(top = 56.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    // 削除ボタン
                    // 削除モーダル表示
                    IconButton(
                        onClick = {

                            // 削除ダイアログ表示
                            isShowDialog.value = true
                        },
                        enabled = !isCheckUnDeletableCategory,
                        content = {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "削除",
                                tint = if (isCheckUnDeletableCategory) Color.Gray else Color.Red
                            )
                        }
                    )

                    // 削除不可注意書きテキスト、使用テキスト判定
                    val notDeleteText = when {
                        isCheckUsedCategory > 0 -> "このカテゴリーは使用されているため\n削除出来ません"
                        isOtherCategory -> "このカテゴリーは削除出来ません"
                        else -> ""
                    }

                    // 削除不可テキスト
                    if (notDeleteText != "") {
                        Text(
                            text = notDeleteText,
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    }
                }

                // 削除ダイアログ
                if (isShowDialog.value) {
                    DeleteDialog(
                        isShowDialog = isShowDialog,
                        title = "${viewModel.editingCategory!!.categoryName}を削除しますか？",
                        onClick = {
                            // 削除

                            // 前の画面に戻る
                            navController.popBackStack()

                            // 削除処理
                            viewModel.deleteCategory(viewModel.editingCategory!!)
                        }
                    )
                }
            }
        }
    }
}