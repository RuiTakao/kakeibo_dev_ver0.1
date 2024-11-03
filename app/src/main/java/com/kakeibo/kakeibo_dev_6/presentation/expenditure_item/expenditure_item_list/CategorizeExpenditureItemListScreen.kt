package com.kakeibo.kakeibo_dev_6.presentation.expenditure_item.expenditure_item_list

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kakeibo.kakeibo_dev_6.common.enum.SelectDate
import com.kakeibo.kakeibo_dev_6.common.utility.priceFormat
import com.kakeibo.kakeibo_dev_6.domain.model.CategorizeExpenditureItem
import com.kakeibo.kakeibo_dev_6.presentation.ScreenRoute
import com.kakeibo.kakeibo_dev_6.presentation.expenditure_item.expenditure_item_list.component.DisplaySwitchArea
import com.kakeibo.kakeibo_dev_6.presentation.expenditure_item.expenditure_item_list.component.ExpenditureItemListTemplate
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * 支出項目リスト　カテゴリー毎
 *
 * @param navController NavController
 * @param viewModel ExpenditureItemListViewModel
 *
 * @return Unit
 */
@Composable
fun CategorizeExpenditureItemListScreen(
    navController: NavController,
    viewModel: ExpenditureItemListViewModel = hiltViewModel()
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
        endDate = selectLastDate
    ).collectAsState(initial = emptyList())

    // 金額合計
    var totalTax = 0
    listItem.forEach {
        totalTax += it.price.toInt()
    }

    ExpenditureItemListTemplate(navController = navController) {
        // 表示切替えエリア
        DisplaySwitchArea(
            totalTax = totalTax,
            viewModel = viewModel
        )



//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.End
//        ) {
//            TextButton(onClick = { /*TODO*/ }) {
//                Row(verticalAlignment = Alignment.CenterVertically) {
//                    Text(text = "明細", color = Color(0xFF854A2A), fontSize = 18.sp)
//                    Icon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "明細",
//                        tint = Color(0xFF854A2A), modifier = Modifier.size(28.dp)
//                    )
//                }
//            }
//        }


//        Tab(navController = navController, page = "item", onClick = {
//
//            // パラメーターに開始日をセット、カスタムの場合はカスタムの開始日をセット
//            val startDate = viewModel.setDateParameter(SelectDate.START)
//
//            // パラメーターに終了日をセット、カスタムの場合はカスタムの終了日をセット
//            val lastDate = viewModel.setDateParameter(SelectDate.LAST)
//
//            // パラメーターに選択機関をセット
//            val dateProperty = viewModel.dateProperty
//
//            // パラメーターにカテゴリーIDセット
//            val categoryId = 0
//
//            // 明細ページへ遷移
//            navController.navigate(
//                ScreenRoute.ExpenditureItemList.route +
//                        "/${categoryId}/${dateProperty}/${startDate}/${lastDate}"
//            )
//        })

        // 支出リスト
        ListItem(
            listItem = listItem,
            navController = navController,
            viewModel = viewModel
        )
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
    viewModel: ExpenditureItemListViewModel
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
                    .shadow(4.dp)
                    .clip(RoundedCornerShape(5.dp))
//                    .border(
//                        width = 1.dp,
//                        color = Color(0xFF854A2A),
//                        shape = RoundedCornerShape(5.dp)
//                    )

                    .background(Color.White)
            ) {
                Row(
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .padding(start = 16.dp)
                        .padding(end = 4.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    // カテゴリー
                    Text(text = it.categoryName, fontSize = 20.sp)

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Column(horizontalAlignment = Alignment.End) {

                            // 金額
                            Text(text = "￥${priceFormat(it.price)}", fontSize = 20.sp)

                            // 支出回数
                            Text(
                                text = "支出回数：${it.categoryId}回",
                                fontSize = 14.sp,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }

                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = "明細",
                            tint = Color(0xFF854A2A),
                            modifier = Modifier
                                .padding(start = 16.dp)
                                .size(32.dp)
                        )
                    }

                }
            }
        }
    }
}