package com.kakeibo.kakeibo_dev_6.presentation.expenditure_item.expenditure_item_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kakeibo.kakeibo_dev_6.common.Colors
import com.kakeibo.kakeibo_dev_6.common.utility.priceFormat
import com.kakeibo.kakeibo_dev_6.common.utility.toDate
import com.kakeibo.kakeibo_dev_6.presentation.ScreenRoute
import com.kakeibo.kakeibo_dev_6.presentation.component.SubTopBar
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * 支出項目　詳細
 *
 * @param navController NavController
 * @param id Int?
 * @param viewModel ExpenditureItemDetailViewModel
 *
 * @return Unit
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenditureItemDetailScreen(
    navController: NavController,
    id: Int?,
    viewModel: ExpenditureItemDetailViewModel = hiltViewModel()
) {

    // 支出項目取得
    val expenditureItem by viewModel.getExpenditureItem(id = id ?: 0)
        .collectAsState(initial = null)

    // 取得した支出項目をViewModelに保存
    LaunchedEffect(expenditureItem) {

        // nullエラー回避
        expenditureItem?.let {
            viewModel.expenditureItem = it
        }
    }

    Scaffold(
        topBar = {
            SubTopBar(
                title = "支出項目 詳細",
                navigation = {
                    IconButton(
                        onClick = {

                            // 前の画面に戻る
                            navController.popBackStack()
                        },
                        content = {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "閉じる",
                                tint = Color(0xFF854A2A)
                            )
                        }
                    )
                },
                actions = {
                    IconButton(
                        onClick = {

                            // 支出項目編集画面に遷移
                            navController.navigate(
                                ScreenRoute.EditExpenditureItem.route + "/${viewModel.expenditureItem?.id ?: 0}"
                            )
                        },
                        content = {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "編集",
                                tint = Color(0xFF854A2A)
                            )
                        }
                    )
                }
            )
        },
        containerColor = Color(Colors.BASE_COLOR),
        modifier = Modifier
            .fillMaxSize()
    ) { paddingValues ->

        // 支出項目詳細表示エリア
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 8.dp)
        ) {

            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White)
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {

                // 日付

                // 詳細項目名
                Text(
                    text = "日付",
                    color = Color.Gray,
                    modifier = Modifier
                        .padding(bottom = 4.dp)
                )

                // 表示フォーマット
                val yMd = SimpleDateFormat("y年M月d日", Locale.JAPANESE)
                // 詳細項目設定　フォーマットに合わせる為、Date型に変換
                val payDate = viewModel.expenditureItem?.let {
                    it.payDate.toDate("yyyy-MM-dd") ?: Date()
                } ?: Date()

                // 詳細項目
                Text(
                    text = yMd.format(payDate),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                // 金額

                // 詳細項目名
                Text(
                    text = "金額",
                    color = Color.Gray,
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .padding(bottom = 4.dp)
                )

                // 詳細項目
                Text(
                    text = "￥${priceFormat(viewModel.expenditureItem?.price ?: "0")}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                // 詳細項目名
                Text(
                    text = "カテゴリー",
                    color = Color.Gray,
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .padding(bottom = 4.dp)
                )

                // カテゴリー一覧取得
                val categoryList by viewModel.categoryList.collectAsState(initial = emptyList())
                // カテゴリー一覧をループ
                categoryList.forEach {
                    // 支出項目から取得したカテゴリーIDと一致するカテゴリー名を取得する
                    if (it.id.toString() == viewModel.expenditureItem?.categoryId) {

                        // 詳細項目
                        Text(
                            text = it.categoryName,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // 詳細項目名
                Text(
                    text = "支出内容",
                    color = Color.Gray,
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .padding(bottom = 4.dp)
                )

                // 詳細項目
                val content = viewModel.expenditureItem?.content ?: ""
                Text(
                    text = if (content != "") content else "？",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // 削除ダイアログ表示非表示切り替え
            val isShowDialog = remember { mutableStateOf(false) }

            // 削除ダイアログ表示ボタン
            IconButton(
                modifier = Modifier
                    .padding(top = 48.dp),
                onClick = {

                    // 削除ダイアログ表示
                    isShowDialog.value = true
                },
                content = {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "削除",
                        tint = Color.Red
                    )
                }
            )

            if (isShowDialog.value) {

                // 削除ダイアログ
                AlertDialog(
                    onDismissRequest = {
                        isShowDialog.value = !isShowDialog.value
                    }
                ) {

                    Column(
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colorScheme.background,
                                shape = MaterialTheme.shapes.extraLarge
                            ),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        // 削除ダイアログタイトル
                        Text(
                            modifier = Modifier
                                .padding(top = 16.dp),
                            text = "支出項目を削除しますか？"
                        )

                        Row(
                            modifier = Modifier
                                .padding(top = 8.dp),
                            horizontalArrangement = Arrangement.End
                        ) {

                            // キャンセルボタン
                            TextButton(
                                onClick = {
                                    isShowDialog.value = false
                                },
                                content = {
                                    Text(text = "キャンセル")
                                }
                            )

                            // 削除ボタン
                            TextButton(
                                onClick = {

                                    // 削除処理
                                    viewModel.expenditureItem?.let {
                                        viewModel.deleteExpenditureItem(it)
                                    }

                                    // 前の画面に戻る
                                    navController.popBackStack()
                                },
                                content = {
                                    Text(text = "削除", color = Color.Red)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}