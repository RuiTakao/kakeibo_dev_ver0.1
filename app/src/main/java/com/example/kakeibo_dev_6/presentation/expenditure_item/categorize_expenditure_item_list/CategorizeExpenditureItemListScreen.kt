package com.example.kakeibo_dev_6.presentation.expenditure_item.categorize_expenditure_item_list

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.kakeibo_dev_6.common.Colors
import com.example.kakeibo_dev_6.common.enum.SelectDate
import com.example.kakeibo_dev_6.domain.model.CategorizeExpenditureItem
import com.example.kakeibo_dev_6.presentation.ScreenRoute
import com.example.kakeibo_dev_6.presentation.expenditure_item.component.DisplaySwitchArea
import com.example.kakeibo_dev_6.presentation.component.FAButton
import com.example.kakeibo_dev_6.presentation.component.MainTopBar
import com.example.kakeibo_dev_6.presentation.expenditure_item.component.ExpenditureListViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun CategorizeExpenditureItemListScreen(
    navController: NavController,
    viewModel: ExpenditureListViewModel = hiltViewModel()
) {
    // クエリ絞り込み用のフォーマット
    val df = SimpleDateFormat("yyyy-MM-dd", Locale.JAPANESE)

    // ログ確認用に変数に格納
    val selectStartDate = df.format(viewModel.selectDate(SelectDate.START))
    val selectLastDate = df.format(viewModel.selectDate(SelectDate.LAST))
    // 日付が期待通りに絞り込まれているかログで確認
    Log.d(
        "支出項目 支出一覧、日付出力範囲",
        "$selectStartDate - $selectLastDate"
    )

    /** DB */
    // カテゴリー毎にグルーピングした支出リストを抽出
    val listItem by viewModel.categorizeExpenditureItem(
        startDate = selectStartDate,
        lastDate = selectLastDate
    ).collectAsState(initial = emptyList())

    // 金額合計
    var totalTax = 0
    listItem.forEach {
        totalTax += it.price.toInt()
    }

    // ドロワーの操作用の変数
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    /* ドロワー用、テンプレート */
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            /* ドロワー */
            Drawer(
                navController = navController,
                drawerState = drawerState,
                scope = scope
            )
        }
    ) {
        Scaffold(
            topBar = {
                MainTopBar(
                    title = "支出項目",
                    navigation = {
                        IconButton(
                            onClick = {
                                // ドロワーの表示非表示の切り替え
                                scope.launch {
                                    drawerState.apply {
                                        if (isClosed) open() else close()
                                    }
                                }
                            },
                            content = {
                                Icon(
                                    imageVector = Icons.Default.Menu,
                                    contentDescription = "メニュー",
                                    tint = Color(0xFF854A2A)
                                )
                            }
                        )
                    },
                    actions = {
                        IconButton(
                            onClick = {
                                // パラメーターに開始日をセット、カスタムの場合はカスタムの開始日をセット
                                val startDate = viewModel.setDateParameter(SelectDate.START)
                                // パラメーターに終了日をセット、カスタムの場合はカスタムの終了日をセット
                                val lastDate = viewModel.setDateParameter(SelectDate.LAST)
                                // パラメーターに選択機関をセット
                                val dateProperty = viewModel.dateProperty
                                // パラメーターにカテゴリーIDセット
                                val categoryId = 0
                                // 明細ページへ遷移
                                navController.navigate(
                                    ScreenRoute.ExpenditureItemList.route +
                                            "/${categoryId}/${dateProperty}/${startDate}/${lastDate}"
                                )
                            },
                            content = {
                                Icon(
                                    imageVector = Icons.Default.ReceiptLong,
                                    contentDescription = "詳細",
                                    tint = Color(0xFF854A2A)
                                )
                            }
                        )
                    }
                )
            },
            floatingActionButton = {
                FAButton(
                    onClick = {
                        // 支出追加ページへ遷移
                        navController.navigate(ScreenRoute.AddExpenditureItem.route)
                    }
                )
            },
            containerColor = Color(Colors.BASE_COLOR),
            modifier = Modifier.fillMaxSize()
        ) {
            Column(modifier = Modifier.padding(it)) {
                /* 表示切替えエリア */
                DisplaySwitchArea(
                    totalTax = totalTax,
                    viewModel = viewModel
                )

                /* 支出リスト */
                ListItem(
                    listItem = listItem,
                    navController = navController,
                    viewModel = viewModel
                )
            }
        }
    }
}


/**
 * 支出リスト
 *
 * @param listItem List<CategorizeExpenditureItem>
 * @param navController NavController
 * @param viewModel ExpenditureListViewModel
 *
 * @return Unit
 */
@Composable
private fun ListItem(
    listItem: List<CategorizeExpenditureItem>,
    navController: NavController,
    viewModel: ExpenditureListViewModel
) {
    LazyColumn(
        modifier = Modifier
            .padding(top = 32.dp)
            .padding(bottom = 80.dp)
    ) {

        // カテゴリー毎にグルーピングし、カテゴリー毎の金額、支出回数の合計のリストを一覧出力
        items(listItem) {
            Column(
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .padding(horizontal = 8.dp)
                    .clickable {
                        // パラメーターに開始日をセット、カスタムの場合はカスタムの開始日をセット
                        val startDate = viewModel.setDateParameter(SelectDate.START)
                        // パラメーターに終了日をセット、カスタムの場合はカスタムの終了日をセット
                        val lastDate = viewModel.setDateParameter(SelectDate.LAST)
                        // パラメーターに選択機関をセット
                        val dateProperty = viewModel.dateProperty
                        // パラメーターにカテゴリーIDセット
                        val categoryId = it.id
                        // 明細ページへ遷移
                        navController.navigate(
                            ScreenRoute.ExpenditureItemList.route +
                                    "/${categoryId}/${dateProperty}/${startDate}/${lastDate}"
                        )
                    }
                    .clip(RoundedCornerShape(5.dp))
                    .background(Color.White)
            ) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    /* カテゴリー */
                    Text(text = it.categoryName, fontSize = 20.sp)
                    Column(horizontalAlignment = Alignment.End) {
                        /* 金額 */
                        Text(text = "￥${it.price}", fontSize = 20.sp)
                        /* 支出回数 */
                        Text(
                            text = "支出回数：${it.categoryId}回",
                            fontSize = 14.sp,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }

}

/**
 * ドロワー
 *
 * @param navController NavController
 * @param drawerState DrawerState
 * @param scope CoroutineScope
 *
 * @return Unit
 */
@Composable
fun Drawer(
    navController: NavController,
    drawerState: DrawerState,
    scope: CoroutineScope
) {
    ModalDrawerSheet(
        modifier = Modifier.width(256.dp),
        drawerShape = MaterialTheme.shapes.extraSmall,
        content = {
            Column(
                modifier = Modifier
                    .background(color = Color(0xFF854A2A))
                    .fillMaxWidth()
                    .height(64.dp),
                verticalArrangement = Arrangement.Center,
                content = {
                    IconButton(
                        onClick = {
                            // ドロワーの表示非表示の切り替え
                            scope.launch {
                                drawerState.apply {
                                    if (isClosed) open() else close()
                                }
                            }
                        },
                        content = {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "閉じる",
                                tint = Color(0xFFEEDCB3)
                            )
                        }
                    )
                }
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF854A2A)),
                content = {
                    TextButton(
                        onClick = {
                            // ドロワー非表示
                            // 遷移後も表示状態になる為、非表示にする処理を入れる
                            scope.launch {
                                drawerState.apply { close() }
                            }
                            // カテゴリー設定へ遷移
                            navController.navigate(ScreenRoute.SettingCategory.route)
                        },
                        modifier = Modifier.padding(top = 16.dp),
                        content = {
                            Text(
                                text = "カテゴリ設定",
                                fontSize = 16.sp,
                                color = Color(0xFFEEDCB3),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    )
                }
            )
        }
    )
}