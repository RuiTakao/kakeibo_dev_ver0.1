package com.kakeibo.kakeibo_dev_6.common.utility.is_registered_user

import com.kakeibo.kakeibo_dev_6.common.Constants
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.TemporalAdjusters
import java.util.Calendar

/**
 * 未来日と二か月前の月の選択を不可にするためのバリデーション
 * 課金ユーザーは無制限
 *
 * @param selectDates Long 選択できる日にち
 *
 * @return Boolean
 */
fun isRegisteredUserReferenceEditDate(selectDates: Long): Boolean {

    // 先月の月の初めを求める変数
    val calendar = Calendar.getInstance()
    // 今月から減算する
    calendar.add(Calendar.MONTH, -1 * Constants.NONE_REGISTERED_USER_ALLOW_REFERENCE_DATE)
    // 先月の日付
    val prevMonth = calendar.time.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
    // 先月の月の初め
    val prevFirstDayOfMonth = prevMonth.with(TemporalAdjusters.firstDayOfMonth())

    // selectDatesをLocalDateに変換
    val selectDatesToLocalDate =
        Instant.ofEpochMilli(selectDates).atZone(ZoneId.systemDefault()).toLocalDate()

    // Bool値を返す
    return if (!Constants.IS_REGISTERED_USER) {
        !selectDatesToLocalDate.isAfter(LocalDate.now()) &&
                !selectDatesToLocalDate.isBefore(prevFirstDayOfMonth)
    } else {
        !selectDatesToLocalDate.isAfter(LocalDate.now())
    }
}