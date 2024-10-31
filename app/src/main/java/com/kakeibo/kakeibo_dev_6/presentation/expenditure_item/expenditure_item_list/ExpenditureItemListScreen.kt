package com.kakeibo.kakeibo_dev_6.presentation.expenditure_item.expenditure_item_list

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kakeibo.kakeibo_dev_6.common.enum.SelectDate
import com.kakeibo.kakeibo_dev_6.presentation.expenditure_item.expenditure_item_list.component.DisplaySwitchArea
import com.kakeibo.kakeibo_dev_6.presentation.expenditure_item.expenditure_item_list.component.ExpenditureItemList
import com.kakeibo.kakeibo_dev_6.presentation.expenditure_item.expenditure_item_list.component.ExpenditureItemListTemplate
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun ExpenditureItemListScreen(
    navController: NavController,
    viewModel: ExpenditureItemListViewModel = hiltViewModel()
) {
    ExpenditureItemListTemplate(navController = navController) {

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

        DisplaySwitchArea(totalTax = totalTax, viewModel = viewModel)

        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
        ) {

            val tabSize = LocalConfiguration.current.screenWidthDp.dp / 2

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(tabSize)
            ) {
                Text(text = "支出項目", color = Color(0xFF854A2A), fontSize = 20.sp, modifier = Modifier.padding(bottom = 4.dp))
                Spacer(modifier = Modifier.height(2.dp).width(tabSize).background(Color(0xFF854A2A)))
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(tabSize)
            ) {
                Text(text = "明細", color = Color.Gray, fontSize = 20.sp, modifier = Modifier.padding(bottom = 4.dp))
                Spacer(modifier = Modifier.height(2.dp).width(tabSize).background(Color.Gray))
            }
        }

        ExpenditureItemList(listItem = listItem, navController = navController, viewModel = viewModel)
    }
}

