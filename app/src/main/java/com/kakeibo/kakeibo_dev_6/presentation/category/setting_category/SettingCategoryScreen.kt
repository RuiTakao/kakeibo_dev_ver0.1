package com.kakeibo.kakeibo_dev_6.presentation.category.setting_category

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.AddBox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kakeibo.kakeibo_dev_6.common.Colors
import com.kakeibo.kakeibo_dev_6.presentation.ScreenRoute
import com.kakeibo.kakeibo_dev_6.presentation.component.DeleteDialog
import com.kakeibo.kakeibo_dev_6.presentation.component.SubTopBar
import kotlinx.coroutines.launch

/**
 * カテゴリー設定画面
 *
 * @param navController NavController
 * @param viewModel SettingCategoryViewModel
 *
 * @return Unit
 */
@Composable
fun SettingCategoryScreen(
    navController: NavController,
    viewModel: SettingCategoryViewModel = hiltViewModel()
) {

    // カテゴリー一覧取得
    val categoryList by viewModel.categoryList.collectAsState(initial = emptyList())

    // カテゴリー並び変えで使用する為、取得したカテゴリー一覧をViewModelに保存
    viewModel.stateCategoryList = categoryList

    // 編集中のカテゴリーが使用されている支出項目取得
    val expenditureItemList by viewModel.expenditureItemList.collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            SubTopBar(
                title = "カテゴリー設定",
                navigation = {
                    IconButton(
                        onClick = {

                            // 前の画面に戻る
                            navController.popBackStack()
                        },
                        content = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "戻る",
                                tint = Color(0xFF854A2A)
                            )
                        }
                    )
                },
                actions = {
                    IconButton(
                        onClick = {

                            // カテゴリー追加画面へ遷移
                            navController.navigate(ScreenRoute.AddCategory.route)
                        },
                        content = {
                            Icon(
                                imageVector = Icons.Outlined.AddBox,
                                contentDescription = "カテゴリ追加",
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

        // カテゴリー一覧
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .padding(top = 24.dp)
                .padding(horizontal = 16.dp)
        ) {

            // カテゴリー一覧表示
            items(items = categoryList) {

                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .padding(bottom = 12.dp)
                        .background(Color.White)
                        .padding(start = 16.dp)
                        .padding(vertical = 4.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    // カテゴリー名
                    Text(
                        text = it.categoryName,
                        fontSize = 20.sp
                    )

                    // コンテキストメニュー表示非表示切り替え
                    val scope = rememberCoroutineScope()
                    var showMenu by remember { mutableStateOf(false) }

                    // 削除ダイアログ表示非表示フラグ
                    val isShowDeleteConfirmDialog = remember { mutableStateOf(false) }

                    // コンテキストメニュー
                    IconButton(
                        onClick = {

                            // コンテキストメニュー表示
                            scope.launch {
                                showMenu.apply {
                                    showMenu = true
                                }
                            }
                        }
                    ) {

                        // コンテキストメニュー表示アイコン
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "編集"
                        )

                        // コンテキストメニュー
                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = {

                                // コンテキストメニュー非表示
                                showMenu = false
                            },
                        ) {
                            TextButton(
                                onClick = {

                                    // コンテキストメニュー非表示
                                    showMenu = false

                                    // 編集画面に遷移
                                    navController.navigate(ScreenRoute.EditCategory.route + "/${it.id}")
                                },
                                modifier = Modifier.padding(horizontal = 8.dp),
                                content = { Text(text = "編集") }
                            )
                            TextButton(
                                onClick = {

                                    // コンテキストメニュー非表示
                                    showMenu = false

                                    // カテゴリー並替え画面に遷移
                                    navController.navigate(ScreenRoute.ReplaceOrderCategory.route)
                                },
                                modifier = Modifier.padding(horizontal = 8.dp),
                                content = { Text(text = "並替え") }
                            )

                            // 削除ダイアログ表示ボタンの表示非表示判定フラグ
                            var notDeleteCategory = false
                            // 支出項目で使用されているまたはその他は削除不可
                            if (it.id == 7) {
                                notDeleteCategory = true
                            } else {
                                expenditureItemList.forEach { expenditureItem ->
                                    if (expenditureItem.categoryId == it.id.toString()) {
                                        notDeleteCategory = true
                                    }
                                }
                            }

                            // 削除ダイアログ表示ボタン
                            if (!notDeleteCategory) {
                                TextButton(
                                    onClick = {

                                        // カテゴリー削除確認ダイアログ表示
                                        isShowDeleteConfirmDialog.value = true

                                        // 選択したカテゴリーを取得
                                        viewModel.category = it
                                    },
                                    modifier = Modifier.padding(horizontal = 8.dp),
                                    content = { Text(text = "削除", color = Color.Red) }
                                )
                            }

                            // カテゴリー削除確認ダイアログ
                            if (isShowDeleteConfirmDialog.value) {

                                DeleteDialog(
                                    isShowDialog = isShowDeleteConfirmDialog,
                                    title = "${viewModel.category?.categoryName}を削除しますか？",
                                    onClick = {
                                        // 削除

                                        // 削除処理
                                        viewModel.category?.let {
                                            viewModel.deleteCategory(it)
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}