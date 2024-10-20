package com.kakeibo.kakeibo_dev_6.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 削除ダイアログ
 *
 * @param isShowDialog MutableState<Boolean> ダイアログ表示非表示の切り替え
 * @param title String ダイアログのタイトル
 * @param onClick () -> Unit 削除処理
 *
 * @return Unit
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteDialog(isShowDialog: MutableState<Boolean>, title: String, onClick: () -> Unit) {
    AlertDialog(
        onDismissRequest = {
            isShowDialog.value = !isShowDialog.value
        }
    ) {

        Column(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.background,
                    shape = MaterialTheme.shapes.extraLarge
                )
                .padding(horizontal = 40.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // 削除ダイアログタイトル
            Text(
                modifier = Modifier
                    .padding(top = 16.dp),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                text = title
            )

            Row(
                modifier = Modifier
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.End
            ) {

                // キャンセルボタン
                TextButton(
                    onClick = {
                        isShowDialog.value = false
                    },
                    content = {
                        Text(
                            text = "キャンセル",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                )
                
                Spacer(modifier = Modifier.width(32.dp))

                // 削除ボタン
                TextButton(
                    onClick = onClick,
                    content = {
                        Text(
                            text = "削除", color = Color.Red,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                )
            }
        }
    }
}