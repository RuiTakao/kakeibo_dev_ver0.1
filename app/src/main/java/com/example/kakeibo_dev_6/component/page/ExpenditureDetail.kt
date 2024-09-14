package com.example.kakeibo_dev_6.component.page

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.kakeibo_dev_6.component.parts.DisplaySwitchArea
import com.example.kakeibo_dev_6.component.parts.FAButton
import com.example.kakeibo_dev_6.component.parts.MainTopBar
import com.example.kakeibo_dev_6.component.utility.toDate
import com.example.kakeibo_dev_6.entity.ExpenditureItem
import com.example.kakeibo_dev_6.entity.ExpenditureItemJoinCategory
import com.example.kakeibo_dev_6.enum.DateProperty
import com.example.kakeibo_dev_6.enum.Route
import com.example.kakeibo_dev_6.enum.SelectDate
import com.example.kakeibo_dev_6.viewModel.ExpenditureListViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun ExpenditureDetail(
    navController: NavController,
    startDate: String? = null,
    lastDate: String? = null,
    categoryId: String? = null,
    dateProperty: String? = null,
    viewModel: ExpenditureListViewModel = hiltViewModel()
) {

    // ステータスバーの色
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setStatusBarColor(color = Color(0xFF854A2A))
    }

    // クエリ絞り込み用のフォーマット
    val df = SimpleDateFormat("yyyy-MM-dd", Locale.JAPANESE)

    /*
    明細遷移時にのみ実行する処理
    支出一覧からのパラメーターを処理するため
     */
    if (viewModel.pageTransitionFlg) {

        // パラメーターから受け取った基準日をViewModelに保存
        viewModel.standardOfStartDate = startDate?.let {
            startDate.toDate("yyyy-MM-dd")
        } ?: viewModel.standardOfStartDate

        // パラメーターから受け取った選択期間をViewModelに保存
        dateProperty?.let { viewModel.dateProperty = dateProperty }
        // パラメーターから受け取ったカテゴリーIDをViewModelに保存
        categoryId?.let { viewModel.selectCategory = categoryId.toInt() }

        // 選択期間がカスタムの場合はパラメーターから受け取った開始日、最終日をViewModelに保存
        if (dateProperty != null && dateProperty == DateProperty.CUSTOM.name) {
            viewModel.customOfStartDate = startDate?.let {
                startDate.toDate("yyyy-MM-dd")
            } ?: viewModel.customOfStartDate

            viewModel.customOfLastDate = lastDate?.let {
                lastDate.toDate("yyyy-MM-dd")
            } ?: viewModel.customOfLastDate
        }

        // 遷移時の処理終了
        viewModel.pageTransitionFlg = false
    }

    // ログ確認用に変数に格納
    val selectStartDate = df.format(viewModel.selectDate(SelectDate.START))
    val selectLastDate = df.format(viewModel.selectDate(SelectDate.LAST))
    // 日付が期待通りに絞り込まれているかログで確認
    Log.d(
        "明細 支出一覧、日付出力範囲", "$selectStartDate - $selectLastDate"
    )

    /** DB */
    // 支出の登録されている日付を取得
    val gropePayDate by viewModel.gropePayDate(
        startDate = selectStartDate,
        lastDate = selectLastDate,
        sort = viewModel.sort,
        categoryId = viewModel.selectCategory
    ).collectAsState(initial = emptyList())

    /** DB */
    // 支出一覧をカテゴリーと結合し抽出
    val expenditureItemList by viewModel.expenditureItemList(
        firstDay = selectStartDate,
        lastDay = selectLastDate,
        sort = viewModel.sort,
        categoryId = viewModel.selectCategory
    ).collectAsState(initial = emptyList())

    // 金額合計
    var totalTax = 0
    expenditureItemList.forEach {
        totalTax += it.price.toInt()
    }

    Scaffold(topBar = {
        MainTopBar(title = "支出項目 明細", navigation = {
            IconButton(onClick = { navController.popBackStack() }, content = {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "戻る",
                    tint = Color(0xFF854A2A)
                )
            })
        }, actions = {})
    }, floatingActionButton = {
        FAButton(onClick = {
            // 支出追加ページに遷移
            navController.navigate(Route.EDIT_EXPENDITURE.name)
        })
    }, content = { padding ->
        Column(modifier = Modifier
            .padding(padding)
            .background(color = Color(0xFFEEDCB3))
            .fillMaxSize(), content = {

            /* 表示切替えエリア */
            DisplaySwitchArea(
                totalTax = totalTax, viewModel = viewModel, searchArea = true
            )

            /* 支出リスト */
            ItemList(
                parentItem = gropePayDate,
                childItemList = expenditureItemList,
                navController = navController
            )
        })
    })
}

@Composable
private fun ItemList(
    parentItem: List<ExpenditureItem>,
    childItemList: List<ExpenditureItemJoinCategory>,
    navController: NavController
) {
    LazyColumn(
        modifier = Modifier.padding(top = 16.dp)
    ) {

        // 支出が登録されている支出日をグルーピングして一覧表示
        items(parentItem) {

            // 支出日を格納
            val parentPayDate = it.payDate
            val df = SimpleDateFormat("yyyy-MM-dd", Locale.JAPANESE)
            val mf = SimpleDateFormat("M月d日", Locale.JAPANESE)

            // 支出日をタイトルとして表示
            Text(
                text = mf.format(it.payDate.toDate("yyyy-MM-dd")!!),
                fontSize = 20.sp,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 4.dp)
                    .padding(top = 16.dp)
            )
            Column(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .background(Color.White),
                content = {

                    // 区切り線の可否判定（グルーピングしたカラムの２行目に区切り線入れる）
                    var rowCount = 0

                    // 支出リスト出力
                    childItemList.forEach {

                        // グルーピングした日付以外の支出リストはスキップ
                        if (parentPayDate != it.payDate) {
                            return@forEach
                        }

                        // ２行目以降は区切り線入れる
                        if (rowCount > 0) {
                            Spacer(
                                modifier = Modifier
                                    .height(1.dp)
                                    .padding(horizontal = 8.dp)
                                    .background(Color.LightGray)
                                    .fillMaxWidth()
                            )
                        }
                        // 区切り線可否の判定後加算
                        rowCount++

                        Row(
                            modifier = Modifier
                                .clickable {
                                    // 支出詳細ページに遷移
                                    navController.navigate(
                                        route = "${Route.EXPENDITURE_ITEM_DETAIL.name}/${it.id}"
                                    )
                                }
                                .padding(vertical = 8.dp, horizontal = 16.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top,
                            content = {
                                Column(
                                    content = {
                                        // 支出内容
                                        Text(
                                            text = it.content,
                                            fontSize = 20.sp,
                                            lineHeight = 0.sp
                                        )
                                        // カテゴリー
                                        Text(
                                            text = it.categoryName,
                                            modifier = Modifier.padding(vertical = 4.dp),
                                            fontSize = 14.sp,
                                            lineHeight = 0.sp
                                        )
                                    }
                                )
                                // 金額
                                Text(text = "￥${it.price}", fontSize = 20.sp)
                            }
                        )
                    }
                }
            )
        }
    }
}