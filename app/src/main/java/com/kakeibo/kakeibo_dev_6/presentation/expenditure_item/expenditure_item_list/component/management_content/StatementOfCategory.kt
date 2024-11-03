package com.kakeibo.kakeibo_dev_6.presentation.expenditure_item.expenditure_item_list.component.management_content

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kakeibo.kakeibo_dev_6.domain.model.Category

/**
 * カテゴリー毎に絞り込み
 *
 * @param categoryList: List<Category>
 * @param id Int
 * @param setId (Int) -> Unit
 *
 * @return Unit
 */
@Composable
fun StatementOfCategory(
    categoryList: List<Category>,
    id: Int,
    setId: (Int) -> Unit
) {

    // コンテキストメニュー表示非表示の判定
    val expanded = remember { mutableStateOf(false) }

    // 選択されているカテゴリーのID
    val selectCategoryId = remember { mutableIntStateOf(id) }

    // 選択されているカテゴリー名の表示
    val selectCategoryName = remember { mutableStateOf("すべて") }

    // 全カテゴリー取得
    // 初期表示時の表示用のカテゴリー名を取得
    categoryList.forEach {
        if (it.id == selectCategoryId.intValue) {
            selectCategoryName.value = it.categoryName
        }
    }

    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable {
                expanded.value = !expanded.value
            }
    ) {

        // 選択したカテゴリー名
        Text(
            text = selectCategoryName.value,
            color = Color.Black,
            fontSize = 20.sp
        )

        // カテゴリー選択アイコン
        Icon(
            imageVector = Icons.Filled.ArrowDropDown,
            contentDescription = "カテゴリー選択ドロップダウン",
            tint = Color(0xFF854A2A),
            modifier = Modifier.size(24.dp)
        )

        // ドロップダウンメニュー
        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = {
                expanded.value = false
            }
        ) {

            // 全カテゴリーを検索するアイテム
            DropdownMenuItem(
                text = { Text(text = "すべて") },
                onClick = {

                    // コンテキストメニューを非表示
                    expanded.value = false

                    // 選択したカテゴリーIDを格納 表示用
                    // 0の場合はすべて
                    selectCategoryId.intValue = 0

                    // 選択したカテゴリー名を格納 表示用
                    selectCategoryName.value = "すべて"

                    // 選択したカテゴリーIDを格納 絞込用
                    // 0の場合はすべて
                    setId(0)
                }
            )

            // 全カテゴリーをコンテキストメニューに表示
            categoryList.forEach {

                // 選択したカテゴリーを検索するアイテム
                DropdownMenuItem(
                    text = { Text(text = it.categoryName) },
                    onClick = {

                        // コンテキストメニューを非表示
                        expanded.value = false

                        // 選択したカテゴリーIDを格納 表示用
                        selectCategoryId.intValue = it.id

                        // 選択したカテゴリー名を格納 表示用
                        selectCategoryName.value = it.categoryName

                        // 選択したカテゴリーIDを格納 絞込用
                        setId(it.id)
                    }
                )
            }
        }
    }
}