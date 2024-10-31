package com.kakeibo.kakeibo_dev_6.presentation.expenditure_item.expenditure_item_list.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kakeibo.kakeibo_dev_6.common.enum.SelectDate
import com.kakeibo.kakeibo_dev_6.common.utility.priceFormat
import com.kakeibo.kakeibo_dev_6.domain.model.CategorizeExpenditureItem
import com.kakeibo.kakeibo_dev_6.presentation.ScreenRoute
import com.kakeibo.kakeibo_dev_6.presentation.expenditure_item.expenditure_item_list.ExpenditureItemListViewModel

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
fun ExpenditureItemList(
    listItem: List<CategorizeExpenditureItem>,
    navController: NavController,
    viewModel: ExpenditureItemListViewModel
) {
    LazyColumn(
        modifier = Modifier
            .padding(top = 16.dp)
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

                    // カテゴリー
                    Text(text = it.categoryName, fontSize = 20.sp)

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
                }
            }
        }
    }
}