package com.kakeibo.kakeibo_dev_6.presentation.expenditure_item.expenditure_item_list.component.management_content

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.kakeibo.kakeibo_dev_6.common.enum.PrevNext

/**
 * 参照する項目の期間を切り替えるボタン
 *
 * @param buttonKind PrevNext ボタン種類
 * @param enabled Boolean クリック可不可の切り替え
 * @param onClick () -> Unit クリック動作
 *
 * @return Unit
 */
@Composable
fun SwitchReferenceDurationButton(buttonKind: PrevNext, enabled: Boolean, onClick: () -> Unit) {

    // ボタンアイコン
    val icon: ImageVector
    // ボタンデスクリプション
    val desc: String

    when (buttonKind) {
        PrevNext.PREV -> {
            icon = Icons.AutoMirrored.Filled.KeyboardArrowLeft
            desc = "前の期間"
        }

        PrevNext.NEXT -> {
            icon = Icons.AutoMirrored.Filled.KeyboardArrowRight
            desc = "次の期間"
        }
    }

    Box(modifier = Modifier.clickable(enabled = enabled, onClick = onClick)) {
        Icon(
            imageVector = icon,
            contentDescription = desc,
            // クリック不可の場合は透過
            tint = if (enabled) Color(0xFF854A2A) else Color.Transparent
        )
    }
}