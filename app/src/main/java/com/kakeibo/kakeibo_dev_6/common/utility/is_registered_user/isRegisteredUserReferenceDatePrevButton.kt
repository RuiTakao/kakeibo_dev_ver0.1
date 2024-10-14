package com.kakeibo.kakeibo_dev_6.common.utility.is_registered_user

import com.kakeibo.kakeibo_dev_6.common.Constants
import java.time.LocalDate

/**
 * Prevボタンのクリック不可判定
 * 二か月前の日付は閲覧不可
 * 課金済の場合は無制限
 *
 * @param standardDate LocalDate
 *
 * @return Boolean
 */
fun isRegisteredUserReferenceDatePrevButton(standardDate: LocalDate): Boolean {
    return if (!Constants.IS_REGISTERED_USER) {
        LocalDate.now().minusMonths(Constants.NONE_REGISTERED_USER_ALLOW_REFERENCE_DATE.toLong())
            .isBefore(standardDate)
    } else {
        true
    }
}