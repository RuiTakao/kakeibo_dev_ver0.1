package com.kakeibo.kakeibo_dev_6.presentation.expenditure_item.expenditure_item_list.component.management_content

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kakeibo.kakeibo_dev_6.common.enum.DayOrWeekOrMonth
import com.kakeibo.kakeibo_dev_6.common.enum.PrevNext
import com.kakeibo.kakeibo_dev_6.common.enum.SwitchDate
import com.kakeibo.kakeibo_dev_6.presentation.expenditure_item.expenditure_item_list.ExpenditureItemListViewModel
import java.util.Date

/**
 * 支出項目一覧の表示切り替えエリア
 *
 * @param totalPaymentTax Int
 * @param viewModel DisplaySwitchAreaViewModel
 * @param statementNavController Boolean 明細ページに遷移するナビゲーション
 * @param containsCategorizeView Boolean カテゴリー毎に表示するかどうかのフラグ
 *
 * @return Unit
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManagementContent(
    totalPaymentTax: Int,
    viewModel: ExpenditureItemListViewModel,
    statementNavController: NavController? = null,
    containsCategorizeView: Boolean = false
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(color = Color(0xFFFEFEEE))
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp)
            .padding(bottom = 16.dp)
    ) {

        // 二行目、三行目のレイアウト
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth() // 幅いっぱいに広げたいためfillMaxWidthをmodifierにセットしておく
        ) {

            // 前へボタン（過去に移動）
            SwitchReferenceDurationButton(
                buttonKind = PrevNext.PREV,
                enabled = viewModel.isPrevButtonEnabled(),
                onClick = {

                    // 過去の日付に移動する処理
                    viewModel.onClickSwitchDateButton(switchAction = SwitchDate.PREV)
                }
            )

            // 真ん中のレイアウト
            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                // 日付の表示（表示期間）
                // 二行目のレイアウト
                Text(
                    text = viewModel.durationDateText(),
                    fontSize = 16.sp
                )
            }

            // 次へボタン（未来へ移動）
            SwitchReferenceDurationButton(
                buttonKind = PrevNext.NEXT,
                enabled = viewModel.isNextButtonEnabled(),
                onClick = {

                    // 未来の日付に移動する処理
                    viewModel.onClickSwitchDateButton(switchAction = SwitchDate.NEXT)
                }
            )
        }

        // 日、週、月、カスタムボタンエリア
        // 一行目のレイアウト
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth() // 幅いっぱいに広げたいためfillMaxWidthをmodifierにセットしておく
                .padding(top = 16.dp)
                .padding(bottom = 16.dp)
        ) {

            /* 日ボタン */
            SwitchReferenceDurationDayOrWeekOrMonthButton(
                dayOrWeekOrMonthProperty = DayOrWeekOrMonth.DAY.name,
                selectedDayOrWeekOrMonth = viewModel.dayOrWeekOrMonthProperty,
                onClick = {

                    // 選択した日付プロパティをViewModelに保存する
                    viewModel.dayOrWeekOrMonthProperty = DayOrWeekOrMonth.DAY.name

                    // 日付の移動後、選択期間を変更した場合、基準日が本日以降の日付になってしまう事がある為
                    // 日、週で切り替えた場合は基準日が本日以降になっていないか確認し、修正する
                    viewModel.initStandardDate()
                }
            )

            // 週ボタン
            SwitchReferenceDurationDayOrWeekOrMonthButton(
                dayOrWeekOrMonthProperty = DayOrWeekOrMonth.WEEK.name,
                selectedDayOrWeekOrMonth = viewModel.dayOrWeekOrMonthProperty,
                onClick = {

                    // 選択した日付プロパティをViewModelに保存する
                    viewModel.dayOrWeekOrMonthProperty = DayOrWeekOrMonth.WEEK.name

                    // 日付の移動後、選択期間を変更した場合、基準日が本日以降の日付になってしまう事がある為
                    // 日、週で切り替えた場合は基準日が本日以降になっていないか確認し、修正する
                    viewModel.initStandardDate()
                }
            )

            // 月ボタン
            SwitchReferenceDurationDayOrWeekOrMonthButton(
                dayOrWeekOrMonthProperty = DayOrWeekOrMonth.MONTH.name,
                selectedDayOrWeekOrMonth = viewModel.dayOrWeekOrMonthProperty,
                onClick = {

                    // 選択した日付プロパティをViewModelに保存する
                    viewModel.dayOrWeekOrMonthProperty = DayOrWeekOrMonth.MONTH.name
                }
            )

            // カスタムボタン

            // DatePickerでデフォルトで設定しておく日付
            val state = rememberDateRangePickerState(
                viewModel.customOfStartDate.time,
                viewModel.customOfEndDate.time
            )

            SwitchReferenceDurationCustomDateButton(
                selectedDateProperty = viewModel.dayOrWeekOrMonthProperty,
                state = state,
                onClick = {

                    // 選択した日付プロパティをViewModelに保存する
                    viewModel.dayOrWeekOrMonthProperty = DayOrWeekOrMonth.CUSTOM.name

                    // 選択した開始日をViewModelのカスタム日付に保存
                    viewModel.customOfStartDate =
                        state.selectedStartDateMillis?.let { Date(it) } ?: Date()

                    // 選択した終了日をViewModelのカスタム日付に保存
                    viewModel.customOfEndDate =
                        state.selectedEndDateMillis?.let { Date(it) } ?: Date()
                }
            )
        }

        Spacer(
            modifier = Modifier
                .height(2.dp)
                .fillMaxWidth()
                .background(color = Color.LightGray)
        )

        if (containsCategorizeView) {

            val categoryList by viewModel.category.collectAsState(initial = emptyList())

            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth()
            ) {

                StatementOfCategory(
                    categoryList = categoryList,
                    id = viewModel.selectCategoryId,
                    setId = {
                        // 選択したカテゴリーIDをViewModelに保存する
                        viewModel.selectCategoryId = it
                    }
                )

                TotalPaymentText(totalPaymentTax = totalPaymentTax, fontSize = 20.sp)
            }
        } else if (statementNavController != null) {

            Box(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                TotalPaymentText(totalPaymentTax = totalPaymentTax)
                TransitionStatementPageButton(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .clickable {

                            // 明細ページへ遷移
                            viewModel.transitionStatementPage(navController = statementNavController)
                        }
                )
            }
        }
    }
}