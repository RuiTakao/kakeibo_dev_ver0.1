package com.kakeibo.kakeibo_dev_6.presentation.expenditure_item.expenditure_item_list.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kakeibo.kakeibo_dev_6.common.Colors
import com.kakeibo.kakeibo_dev_6.presentation.ScreenRoute
import com.kakeibo.kakeibo_dev_6.presentation.component.FAButton
import com.kakeibo.kakeibo_dev_6.presentation.component.MainTopBar
import kotlinx.coroutines.launch

@Composable
fun ExpenditureItemListTemplate(
    navController: NavController,
    content: @Composable() (ColumnScope.() -> Unit)
) {
    // ドロワーの操作用の変数
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // ドロワー用、テンプレート
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            // ドロワー
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
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
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
                                modifier = Modifier.padding(top = 8.dp),
                                content = {
                                    Text(
                                        text = "カテゴリ設定",
                                        fontSize = 16.sp,
                                        color = Color(0xFFEEDCB3),
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 8.dp)
                                    )
                                }
                            )
                        }
                    )
                }
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
                    actions = {}
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
            modifier = Modifier.fillMaxSize(),
            content = {
                Column(modifier = Modifier.padding(it), content = content)
            }
        )
    }
}