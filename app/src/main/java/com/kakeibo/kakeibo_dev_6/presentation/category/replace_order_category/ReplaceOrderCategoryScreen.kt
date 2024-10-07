package com.kakeibo.kakeibo_dev_6.presentation.category.replace_order_category

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kakeibo.kakeibo_dev_6.common.Colors
import com.kakeibo.kakeibo_dev_6.presentation.category.setting_category.SettingCategoryViewModel
import com.kakeibo.kakeibo_dev_6.presentation.component.SubTopBar
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable

/**
 * カテゴリー並替え
 *
 * @param navController NavController
 * @param viewModel ReplaceOrderCategoryViewModel
 * @param settingCategoryViewModel SettingCategoryViewModel
 *
 * @return Unit
 */
@Composable
fun ReplaceOrderCategoryScreen(
    navController: NavController,
    viewModel: ReplaceOrderCategoryViewModel = hiltViewModel(),
    settingCategoryViewModel: SettingCategoryViewModel
) {

    // カテゴリー一覧取得
    val categoryList = remember { mutableStateOf(settingCategoryViewModel.stateCategoryList) }

    // カテゴリー並び替えUI
    val state = rememberReorderableLazyListState(
        onMove = { from, to ->

            categoryList.value?.let {

                // カテゴリーをUIで入れ替えるとリストの中身も入れ替える処理
                categoryList.value = it.toMutableList().apply {
                    add(to.index, removeAt(from.index))
                }

                // 入れ替わったカテゴリーをViewModelに保存
                viewModel.stateCategoryList = categoryList.value
            }
        }
    )

    Scaffold(
        topBar = {
            SubTopBar(
                title = "カテゴリー並替え",
                navigation = {
                    IconButton(
                        onClick = {

                            // 前の画面に戻る
                            navController.popBackStack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "戻る",
                            tint = Color(0xFF854A2A)
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {

                            // カテゴリーの一括更新処理　並び順更新
                            viewModel.stateCategoryList?.let {

                                // リストに格納されているカテゴリーを一つずつ取得し、並び順番号を付与していく
                                // 並び順は降順で並べていくので、リストの最初のカテゴリーの並び順番号にはリストのサイズ数の値を付与
                                // 二番目以降はリストのサイズ数から一ずつ減算した値を付与していく

                                // カテゴリー数を取得
                                var categoryListSize = viewModel.stateCategoryList?.size ?: 0

                                // nullエラー対策
                                // カテゴリー数が0の場合は処理しない
                                if (categoryListSize != 0) {

                                    // リストだと0スタートになるので数値を合わせる為、カテゴリー数に+1する
                                    categoryListSize++

                                    // カテゴリー一覧をループして並び順を更新する
                                    viewModel.stateCategoryList?.forEach {

                                        // 並び順格納
                                        viewModel.categoryOrder = categoryListSize

                                        // 並び順更新処理
                                        viewModel.updateCategory(it)

                                        // 次ループのカテゴリーの並び順に減算した並び順番号を格納する為、カテゴリー数に-1する
                                        categoryListSize--
                                    }
                                }
                            }

                            // 前の画面に戻る
                            navController.popBackStack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "登録",
                            tint = Color(0xFF854A2A)
                        )
                    }
                }
            )
        },
        containerColor = Color(Colors.BASE_COLOR),
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->

        Column(modifier = Modifier.padding(paddingValues)) {

            // 注意書きテキスト
            Text(
                text = "長押しでカテゴリーの並替えができます",
                color = Color.Gray,
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .padding(top = 8.dp)
            )

            // カテゴリー一覧を表示
            LazyColumn(
                state = state.listState,
                modifier = Modifier
                    .reorderable(state)
                    .detectReorderAfterLongPress(state)
                    .padding(top = 8.dp)
                    .fillMaxSize()
            ) {

                // カテゴリー一覧をループ
                items(categoryList.value!!, { it.id }) {

                    // 並び替えUIのComposable関数
                    ReorderableItem(
                        reorderableState = state,
                        key = it.id,
                        modifier = Modifier
                            .padding(bottom = 12.dp)
                            .padding(horizontal = 12.dp)
                            .fillMaxWidth()
                    ) { isDragging ->

                        // ドラッグドロップ時の影の操作
                        val elevation = animateDpAsState(
                            targetValue = if (isDragging) 16.dp else 4.dp,
                            label = "ドラッグドロップ時の影"
                        )

                        Row(
                            modifier = Modifier
                                .shadow(elevation.value)
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color.White)
                                .padding(start = 16.dp)
                                .padding(vertical = 16.dp)
                                .fillMaxWidth()
                        ) {

                            // カテゴリー名
                            Text(text = it.categoryName, fontSize = 20.sp)
                        }
                    }
                }
            }
        }
    }
}