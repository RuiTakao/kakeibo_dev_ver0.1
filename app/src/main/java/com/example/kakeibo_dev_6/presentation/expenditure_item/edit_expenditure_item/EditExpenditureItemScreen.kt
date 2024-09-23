package com.example.kakeibo_dev_6.presentation.expenditure_item.edit_expenditure_item

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.kakeibo_dev_6.common.Colors
import com.example.kakeibo_dev_6.presentation.ScreenRoute
import com.example.kakeibo_dev_6.presentation.component.expenditure_item.InputCategoryScreen
import com.example.kakeibo_dev_6.presentation.component.expenditure_item.InputContentScreen
import com.example.kakeibo_dev_6.presentation.component.expenditure_item.InputPayDateScreen
import com.example.kakeibo_dev_6.presentation.component.expenditure_item.InputPriceScreen
import com.example.kakeibo_dev_6.presentation.component.SubTopBar
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * 支出項目追加画面
 * 支出項目編集画面
 *
 * @param navController NavController ナビゲーション用のインスタンス
 * @param id Int? 編集の場合、支出項目id　追加の場合はnull
 * @param viewModel EditExpenditureItemViewModel　EditExpenditureItemViewModelのインスタンス
 *
 * @return Unit
 */
@Composable
fun EditExpenditureItemScreen(
    navController: NavController,
    id: Int? = null,
    viewModel: EditExpenditureItemViewModel = hiltViewModel()
) {

    val df = SimpleDateFormat("yyyy-MM-dd", Locale.JAPANESE)
    val payDate = remember { mutableStateOf("") }
    val price = remember { mutableStateOf("") }
    val categoryId = remember { mutableStateOf("") }
    val content = remember { mutableStateOf("") }

    // idがnullかnotNullで追加画面、編集画面の判定をする
    if (id != null) {
        // 編集

        // idから支出項目の内容を取得する
        val editExpendItem by viewModel.setEditingExpendItem(id = id)
            .collectAsState(initial = null)

        // 支出項目読み込み後、各フィールドにデータ挿入する
        LaunchedEffect(editExpendItem) {
            // nullエラー回避処理
            editExpendItem?.let {
                // 日付　DatePickerを開いたとき、前日の日付と被ってズレる為、12:00:00を付け加える
                payDate.value = it.payDate + " 12:00:00"
                // 金額
                price.value = it.price
                // カテゴリーID
                categoryId.value = it.categoryId
                // 内容
                content.value = it.content
            }

            // 支出項目の入力内容保持
            viewModel.editingExpendItem = editExpendItem
        }
    } else {
        // 登録

        //登録の場合は日付に本日の日付を入れておく
        payDate.value = df.format(Date())
    }

    Scaffold(
        topBar = {
            SubTopBar(
                title = "支出項目" + if (id != null) "編集" else "追加",
                navigation = {
                    IconButton(onClick = { navController.popBackStack() }) {
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
                            if (viewModel.validate(
                                    payDate = payDate.value,
                                    price = price.value,
                                    categoryId = categoryId.value,
                                    content = content.value
                                )
                            ) {
                                // バリデーションエラー無し

                                // 前の画面に戻る
                                navController.popBackStack()

                                // 各値の登録
                                viewModel.payDate = payDate.value
                                viewModel.price = price.value
                                viewModel.categoryId = categoryId.value
                                viewModel.content = content.value

                                // idがnullかnotNullで追加処理、編集処理の判定をする
                                if (id != null) {
                                    // 更新
                                    viewModel.updateExpendItem()
                                } else {
                                    // 登録
                                    viewModel.createExpendItem()
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
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(horizontal = 16.dp)
                .padding(top = 32.dp)
        ) {

            // 日付
            InputPayDateScreen(
                payDate = payDate,
                validationMsg = viewModel.inputValidatePayDateText
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 金額
            InputPriceScreen(price = price, validationMsg = viewModel.inputValidatePriceText)

            Spacer(modifier = Modifier.height(16.dp))

            // カテゴリー
            val categoryList by viewModel.category.collectAsState(initial = emptyList())
            InputCategoryScreen(
                categoryId = categoryId,
                categoryList = categoryList,
                addCategoryOrder = viewModel.addCategoryOrder,
                validationMsg = viewModel.inputValidateSelectCategoryText,
                onClickAddCategory = {
                    // カテゴリー追加画面への遷移
                    // 支出登録、編集画面のViewModelを引き継いで遷移

                    // idがnullかnotNullで追加画面からのルーティング、編集画面からのルーティングの判定をする
                    if (id != null) {

                        // 編集画面からのルーティング
                        navController.navigate(ScreenRoute.AddCategoryFromEditExpenditureItem.route + "/${id}")
                    } else {

                        // 追加画面からのルーティング
                        navController.navigate(ScreenRoute.AddCategoryFromAddExpenditureItem.route)
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 内容
            InputContentScreen(
                content = content,
                validationMsg = viewModel.inputValidateContentText
            )
        }
    }
}