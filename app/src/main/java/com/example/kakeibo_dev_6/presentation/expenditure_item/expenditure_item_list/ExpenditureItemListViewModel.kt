package com.example.kakeibo_dev_6.presentation.expenditure_item.expenditure_item_list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.kakeibo_dev_6.common.enum.DateProperty
import com.example.kakeibo_dev_6.common.enum.SelectDate
import com.example.kakeibo_dev_6.common.enum.SwitchDate
import com.example.kakeibo_dev_6.common.utility.toDate
import com.example.kakeibo_dev_6.domain.model.CategorizeExpenditureItem
import com.example.kakeibo_dev_6.domain.model.ExpenditureItem
import com.example.kakeibo_dev_6.domain.model.ExpenditureItemJoinCategory
import com.example.kakeibo_dev_6.domain.repository.CategorizeExpenditureItemDao
import com.example.kakeibo_dev_6.domain.repository.CategoryDao
import com.example.kakeibo_dev_6.domain.repository.ExpenditureItemDao
import com.example.kakeibo_dev_6.domain.repository.ExpenditureItemJoinCategoryDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject


@HiltViewModel
class ExpenditureItemListViewModel @Inject constructor(
    private val categorizeExpenditureItemDao: CategorizeExpenditureItemDao,
    private val expenditureItemJoinCategoryDao: ExpenditureItemJoinCategoryDao,
    categoryDao: CategoryDao,
    private val expenditureItemDao: ExpenditureItemDao
) : ViewModel() {

    /** カテゴリー毎・明細　共通処理 */

    // 表示期間選択ステータス
    var dateProperty by mutableStateOf(DateProperty.WEEK.name)
    var selectCategoryId by mutableIntStateOf(0)

    // 支出日の並び順のステータス
    var sortOfPayDate by mutableStateOf(false)

    // カテゴリー毎から明細へ遷移した時
    var pageTransitionFlg by mutableStateOf(true)

    val category = categoryDao.loadAllCategories().distinctUntilChanged()

    // 表示期間の基準日
    var standardOfStartDate by mutableStateOf(Date())

    // カスタム日選択時の開始日
    var customOfStartDate by mutableStateOf(defaultCustomOfStartDate())

    // カスタム日選択時の終了日
    var customOfEndDate by mutableStateOf(Date())

    private fun defaultCustomOfStartDate(): Date {
        val firstDayOfMonth = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth())
        val dateStr =
            firstDayOfMonth.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " 12:00:00"
        return dateStr.toDate()?.let { dateStr.toDate() } ?: Date()
    }

    fun selectDate(selectDate: SelectDate): Date {
        val getDate = Calendar.getInstance()
        getDate.time = standardOfStartDate

        when (dateProperty) {
            DateProperty.DAY.name -> return getDate.time

            DateProperty.WEEK.name -> {
                val amount = when (selectDate) {
                    SelectDate.START -> 1
                    SelectDate.LAST -> 7
                }
                getDate.add(Calendar.DATE, getDate.get(Calendar.DAY_OF_WEEK) * -1 + amount)
                return getDate.time
            }

            DateProperty.MONTH.name -> {
                val standardOfToLocalDate =
                    getDate.time.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                val monthOfDate = when (selectDate) {
                    SelectDate.START -> standardOfToLocalDate.with(TemporalAdjusters.firstDayOfMonth())
                    SelectDate.LAST -> standardOfToLocalDate.with(TemporalAdjusters.lastDayOfMonth())
                }
                return Date.from(monthOfDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
            }

            DateProperty.CUSTOM.name -> {
                return when (selectDate) {
                    SelectDate.START -> customOfStartDate
                    SelectDate.LAST -> customOfEndDate
                }
            }
        }
        return Date()
    }

    /**
     * カテゴライズした支出項目リスト
     *
     * @param startDate String
     * @param endDate String
     *
     * @return Flow<List<CategorizeExpenditureItem>>
     */
    fun categorizeExpenditureItem(
        startDate: String,
        endDate: String
    ): Flow<List<CategorizeExpenditureItem>> =
        categorizeExpenditureItemDao.categorizeExpenditureItem(
            startDate = startDate,
            endDate = endDate
        ).distinctUntilChanged()


    /**
     * 支出日でグループ化した支出項目リスト
     *
     * @param startDate String
     * @param endDate String
     * @param categoryId Int
     * @param sortOfPayDate Boolean
     *
     * @return Flow<List<ExpenditureItem>>
     */
    fun expenditureItemListGropeByPayDate(
        startDate: String,
        endDate: String,
        categoryId: Int,
        sortOfPayDate: Boolean
    ): Flow<List<ExpenditureItem>> =
        if (sortOfPayDate) {
            expenditureItemDao.gropePayDateAsc(
                startDate = startDate,
                endDate = endDate,
                categoryId = categoryId
            ).distinctUntilChanged()
        } else {
            expenditureItemDao.gropePayDateDesc(
                startDate = startDate,
                endDate = endDate,
                categoryId = categoryId
            ).distinctUntilChanged()
        }

    /**
     * 支出項目リスト
     *
     * @param startDate String
     * @param endDate String
     * @param categoryId Int
     * @param sortOfPayDate Boolean
     *
     * @return Flow<List<ExpenditureItemJoinCategory>>
     */
    fun expenditureItemList(
        startDate: String,
        endDate: String,
        categoryId: Int,
        sortOfPayDate: Boolean
    ): Flow<List<ExpenditureItemJoinCategory>> {
        return if (sortOfPayDate) {
            expenditureItemJoinCategoryDao.loadAllExpenditureItemOrderAsc(
                startDate = startDate,
                endDate = endDate,
                categoryId = categoryId
            )
                .distinctUntilChanged()
        } else {
            expenditureItemJoinCategoryDao.loadAllExpenditureItemOrderDesc(
                startDate = startDate,
                endDate = endDate,
                categoryId = categoryId
            )
                .distinctUntilChanged()
        }
    }

    /**
     * 基準日の初期化
     * 日付の移動後、選択期間を変更した場合、基準日が本日以降の日付になってしまう事がある為
     * 日、週で切り替えた場合は基準日が本日以降になっていないか確認し、修正する
     *
     * @return Unit
     */
    fun initStandardDate() {

        // 基準日をLocalDate型で取得
        val standardDate =
            standardOfStartDate.toInstant().atZone(ZoneId.systemDefault())
                .toLocalDate()

        // 基準日が本日以降の場合は基準日を本日にする
        if (standardDate.isAfter(LocalDate.now())) {
            standardOfStartDate = Date()
        }
    }

    /**
     * 表示期間のテキスト
     *
     * @return String
     */
    fun durationDateText(): String {
        // 日付フォーマット
        val df = SimpleDateFormat("M月d日", Locale.JAPANESE)
        val mf = SimpleDateFormat("M月", Locale.JAPANESE)

        when (dateProperty) {

            /* 日 */
            DateProperty.DAY.name -> return df.format(standardOfStartDate)

            /* 週 */
            DateProperty.WEEK.name -> {
                // 基準日の週の開始日と終了日を求める為、基準日をカレンダークラスのインスタンスに格納する
                val getDate = Calendar.getInstance()
                getDate.time = standardOfStartDate
                // 基準日が何曜日か数字で取得
                val dayOfWeek = getDate.get(Calendar.DAY_OF_WEEK)
                // 基準日から曜日番号を減算し、１加算して基準日の週初めの日付を求める
                getDate.add(Calendar.DATE, dayOfWeek * -1 + 1)
                // 開始日を保存、終了日の計算でgetDateの値が変動するので、ここで保存する
                val startDate = getDate.time
                // getDateはここでは、基準日の週初めの日付になっているため、６加算したら週終わりの日付になる
                getDate.add(Calendar.DATE, 6)
                // 終了日を保存
                val lastDate = getDate.time
                return "${df.format(startDate)} 〜 ${df.format(lastDate)}"
            }

            /* 月 */
            // 日付フォーマットで月のみ取得
            DateProperty.MONTH.name -> return mf.format(standardOfStartDate)

            /* カスタム */
            // カスタム日付用に保存している開始日と終了日をセット
            DateProperty.CUSTOM.name ->
                return "${df.format(customOfStartDate)} 〜 ${df.format(customOfEndDate)}"
        }
        return ""
    }

    /**
     * Nextボタンのクリック不可判定
     *
     * @return Unit
     */
    fun isNextButtonEnabled(): Boolean {

        // 比較用に基準日をLocalDate型にしておく
        val standardDate =
            standardOfStartDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()

        if (
            dateProperty == DateProperty.CUSTOM.name || // カスタムが選択されている場合はクリック不可
            standardDate.isEqual(LocalDate.now()) // 日付が本日の場合はクリック不可
        ) {
            return false
        } else {
            when (dateProperty) {

                // 週が選択されている場合の判定
                DateProperty.WEEK.name -> {

                    // 基準日をカレンダーインスタンスに格納する
                    val getDate = Calendar.getInstance()
                    getDate.time = standardOfStartDate
                    // 基準日の曜日を数字で取得
                    val dayOfWeek = getDate.get(Calendar.DAY_OF_WEEK)
                    // 基準日の週の最終日を求める
                    getDate.add(Calendar.DATE, dayOfWeek * -1 + 7)
                    val weekOfLastDate =
                        getDate.time.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                    // 本日の週の最終日が本日以降であればクリック不可
                    if (weekOfLastDate.isAfter(LocalDate.now())) {
                        return false
                    }
                }

                // 月が選択されている場合の判定
                DateProperty.MONTH.name -> {
                    // 本日の月の開始日
                    val nowIsFirstDayOfMonth =
                        LocalDate.now().with(TemporalAdjusters.firstDayOfMonth())
                    // 基準日の月の開始日
                    val standardDayOfFirstDayOfMonth =
                        standardDate.with(TemporalAdjusters.firstDayOfMonth())
                    // 本日の月と基準日の月が同じ場合はクリック不可
                    if (nowIsFirstDayOfMonth.isEqual(standardDayOfFirstDayOfMonth)) {
                        return false
                    }
                }
            }
        }
        return true
    }

    /**
     * 日付切り替え
     *
     * @param switchAction SwitchDate prevかnextかの判定
     *
     * @return Unit
     */
    fun onClickSwitchDateButton(switchAction: SwitchDate) {

        // カレンダーインスタンス作成
        val getDate = Calendar.getInstance()

        when (dateProperty) {

            /* 日 */
            DateProperty.DAY.name -> {

                /*
                datePropertyが日の場合は開始日を基準に計算
                prevの場合は１日減算して開始日と終了日にセット
                nextの場合は１日加算して開始日と終了日にセット
                 */
                getDate.time = standardOfStartDate
                val amount =
                    when (switchAction) {
                        SwitchDate.PREV -> -1
                        SwitchDate.NEXT -> 1
                    }
                getDate.add(Calendar.DATE, amount)
                standardOfStartDate = getDate.time
            }

            /* 週 */
            DateProperty.WEEK.name -> {

                /*
                datePropertyが週の場合は開始日を基準に計算
                prevの場合は１日減算して開始日と終了日にセット
                nextの場合は１日加算して開始日と終了日にセット
                 */
                getDate.time = standardOfStartDate
                val amount =
                    when (switchAction) {
                        SwitchDate.PREV -> -7
                        SwitchDate.NEXT -> 7
                    }
                getDate.add(Calendar.DATE, amount)
                standardOfStartDate = getDate.time
            }

            /* 月 */
            DateProperty.MONTH.name -> {

                /*
                datePropertyが月の場合は開始日を基準に計算
                prevの場合はgetDateから１月減算
                nextの場合はgetDateから１月加算
                 */
                getDate.time = standardOfStartDate
                val amount =
                    when (switchAction) {
                        SwitchDate.PREV -> -1
                        SwitchDate.NEXT -> 1
                    }
                getDate.add(Calendar.MONTH, amount)
                standardOfStartDate = getDate.time
            }
        }
    }

    /**
     * 明細ページ遷移用のパラメーターセット
     *
     * @param selectDate SelectDate
     *
     * @return String
     */
    fun setDateParameter(selectDate: SelectDate): String {
        val df = SimpleDateFormat("yyyy-MM-dd", Locale.JAPANESE)

        return if (dateProperty == DateProperty.CUSTOM.name) {
            when (selectDate) {
                SelectDate.START -> df.format(customOfStartDate)
                SelectDate.LAST -> df.format(customOfEndDate)
            }
        } else {
            df.format(standardOfStartDate)
        }
    }
}