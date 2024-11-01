package com.kakeibo.kakeibo_dev_6.presentation.expenditure_item.expenditure_item_list.component

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kakeibo.kakeibo_dev_6.common.enum.DateProperty
import com.kakeibo.kakeibo_dev_6.common.enum.SelectDate
import com.kakeibo.kakeibo_dev_6.common.utility.priceFormat
import com.kakeibo.kakeibo_dev_6.common.utility.toDate
import com.kakeibo.kakeibo_dev_6.domain.model.ExpenditureItem
import com.kakeibo.kakeibo_dev_6.domain.model.ExpenditureItemJoinCategory
import com.kakeibo.kakeibo_dev_6.presentation.ScreenRoute
import com.kakeibo.kakeibo_dev_6.presentation.expenditure_item.expenditure_item_list.ExpenditureItemListViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun ExpenditureStatement(navController: NavController, viewModel: ExpenditureItemListViewModel) {
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
    // 支出の登録されている日付を取得
    val expenditureItemListGropeByPayDate by viewModel.expenditureItemListGropeByPayDate(
        startDate = selectStartDate,
        endDate = selectLastDate,
        sortOfPayDate = viewModel.sortOfPayDate,
        categoryId = viewModel.selectCategoryId
    ).collectAsState(initial = emptyList())

    /** DB */
    // 支出一覧をカテゴリーと結合し抽出
    val expenditureItemList by viewModel.expenditureItemList(
        startDate = selectStartDate,
        endDate = selectLastDate,
        sortOfPayDate = viewModel.sortOfPayDate,
        categoryId = viewModel.selectCategoryId
    ).collectAsState(initial = emptyList())

    // 金額合計
    var totalTax = 0
    expenditureItemList.forEach {
        totalTax += it.price.toInt()
    }

    DisplaySwitchArea(totalTax = totalTax, viewModel = viewModel)

    Tab(viewModel = viewModel)

    ItemList(
        parentItem = expenditureItemListGropeByPayDate,
        dateProperty = viewModel.dateProperty,
        childItemList = expenditureItemList,
        clickable = {
            // 支出詳細ページに遷移
            navController.navigate(
                route = ScreenRoute.ExpenditureItemDetail.route + "/${it}"
            )
        }
    )
}

@Composable
private fun ItemList(
    parentItem: List<ExpenditureItem>,
    dateProperty: String,
    childItemList: List<ExpenditureItemJoinCategory>,
    clickable: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .padding(top = 16.dp)
            .padding(bottom = 80.dp)
    ) {

        // 支出が登録されている支出日をグルーピングして一覧表示
        items(parentItem) {

            // 支出日を格納
            val parentPayDate = it.payDate
            val mf = SimpleDateFormat("M月d日", Locale.JAPANESE)

            if (dateProperty != DateProperty.DAY.name) {

                // 支出日をタイトルとして表示
                Text(
                    text = mf.format(it.payDate.toDate("yyyy-MM-dd")!!),
                    fontSize = 20.sp,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 4.dp)
                        .padding(top = 16.dp)
                )
            } else {
                Spacer(modifier = Modifier.height(16.dp))
            }

            Column(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .background(Color.White),
            ) {
                ChildItemList(
                    childItemList = childItemList,
                    parentPayDate = parentPayDate,
                    clickable = { id -> clickable(id) }
                )
            }
        }
    }
}

@Composable
private fun ChildItemList(
    childItemList: List<ExpenditureItemJoinCategory>,
    parentPayDate: String,
    clickable: (Int) -> Unit
) {
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
                .clickable { clickable(it.id) }
                .padding(vertical = 8.dp, horizontal = 8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top,
            content = {
                Column(
                    content = {

                        // 支出内容
                        Text(
                            text = if (it.content != "") it.content else "？",
                            fontSize = 20.sp,
                            lineHeight = 0.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.width(200.dp)
                        )

                        // カテゴリー
                        Text(
                            text = it.categoryName,
//                            modifier = Modifier.padding(16.dp),
                            fontSize = 14.sp,
                            lineHeight = 0.sp
                        )
                    }
                )

                // 金額
                Text(text = "￥${priceFormat(it.price)}", fontSize = 20.sp)
            }
        )
    }
}