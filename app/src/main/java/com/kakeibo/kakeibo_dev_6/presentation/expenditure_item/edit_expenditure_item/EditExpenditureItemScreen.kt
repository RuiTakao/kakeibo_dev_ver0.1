package com.kakeibo.kakeibo_dev_6.presentation.expenditure_item.edit_expenditure_item

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kakeibo.kakeibo_dev_6.common.Colors
import com.kakeibo.kakeibo_dev_6.common.utility.is_registered_user.isRegisteredUserReferenceEditDate
import com.kakeibo.kakeibo_dev_6.common.utility.toDate
import com.kakeibo.kakeibo_dev_6.presentation.ScreenRoute
import com.kakeibo.kakeibo_dev_6.presentation.component.SubTopBar
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

/**
 * 支出項目追加画面
 * 支出項目編集画面
 *
 * @param navController NavController ナビゲーション用のインスタンス
 * @param id Int? 編集の場合、支出項目id　追加の場合はnull
 * @param viewModel EditExpenditureItemViewModel　EditExpenditureItemViewModelのインスタンス
 *
 * @return Unit
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditExpenditureItemScreen(
    navController: NavController,
    id: Int? = null,
    viewModel: EditExpenditureItemViewModel = hiltViewModel()
) {

    // 背景クリックしたらフォーカスを外す処理の為の変数
    val outFocusRequester = remember { FocusRequester() }
    val interactionSource = remember { MutableInteractionSource() }

    // フォーカスを当てる変数
    val focusRequester = remember { FocusRequester() }

    // フォーカスを末尾に配置する為の変数
    var textFieldValue by remember {
        mutableStateOf(
            TextFieldValue(
                text = "",
                selection = TextRange(0)
            )
        )
    }

    // idがnullかnotNullで追加画面、編集画面の判定をする
    if (id != null) {
        // 編集

        // idから支出項目の内容を取得する
        val editExpendItem by viewModel.setEditingExpendItem(id = id)
            .collectAsState(initial = null)

        // 支出項目読み込み後、各フィールドにデータ挿入する
        LaunchedEffect(editExpendItem) {
            // nullエラー回避処理
            editExpendItem?.let {

                // カテゴリーID
                viewModel.categoryId = it.categoryId

                // カテゴリー追加から戻って来た場合は初期値設定しない
                if (!viewModel.addCategoryFlg) {

                    // 日付　DatePickerを開いたとき、前日の日付と被ってズレる為、12:00:00を付け加える
                    val df = SimpleDateFormat("yyyy-MM-dd", Locale.JAPANESE)
                    val localDate = LocalDate.parse(
                        df.format(it.payDate.toDate("yyyy-MM-dd")!!),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd")
                    )

                    viewModel.payDate =
                        localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd 12:00:00"))

                    // 金額
                    // TextFieldValue型で扱う
                    textFieldValue = TextFieldValue(text = it.price, selection = TextRange(it.price.length))
                    viewModel.price = textFieldValue.text

                    // 内容
                    viewModel.content = it.content
                }

            }

            // 支出項目の入力内容保持
            viewModel.editingExpendItem = editExpendItem
        }
    } else {
        // 登録

        // カテゴリー追加から戻って来た場合は初期値設定しない
        if (!viewModel.addCategoryFlg) {

            //登録の場合は日付に本日の日付を入れておく
            val df = SimpleDateFormat("yyyy-MM-dd", Locale.JAPANESE)
            viewModel.payDate = df.format(Date())
        }
    }

    Scaffold(
        topBar = {
            SubTopBar(
                title = "支出項目" + if (id != null) "編集" else "追加",
                navigation = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "閉じる",
                            tint = Color(0xFF854A2A)
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            // 登録処理

                            // バリデーション判定
                            if (viewModel.validate()) {
                                // バリデーションエラー無し

                                // 前の画面に戻る
                                navController.popBackStack()
                                val df = SimpleDateFormat("yyyy-MM-dd", Locale.JAPANESE)

                                val localDate = LocalDate.parse(
                                    df.format(viewModel.payDate.toDate("yyyy-MM-dd")!!),
                                    DateTimeFormatter.ofPattern("yyyy-MM-dd")
                                )

                                viewModel.payDate =
                                    localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

                                Log.d("保存時のpayDate確認", viewModel.payDate)
                                // idがnullかnotNullで追加処理、編集処理の判定をする
                                if (id != null) {
                                    // 更新
                                    viewModel.updateExpendItem()
                                } else {
                                    // 登録
                                    viewModel.createExpendItem()
                                }
                            }
                        },
                        content = {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "登録",
                                tint = Color(0xFF854A2A)
                            )
                        }
                    )
                }
            )
        },
        containerColor = Color(Colors.BASE_COLOR),
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                interactionSource = interactionSource,
                enabled = true,
                indication = null,
                onClick = {outFocusRequester.requestFocus()}
            )
            .focusRequester(outFocusRequester)
            .focusTarget()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .padding(top = 32.dp)
        ) {

            // 日付

            // 日付選択ダイアログ表示非表示判定
            var visible by remember { mutableStateOf(false) }

            // 入力項目名
            Text(
                text = "日付",
                modifier = Modifier.padding(bottom = 4.dp),
                fontWeight = FontWeight.Bold
            )

            // 日付選択フィールド
            Box(
                modifier = Modifier
                    .size(320.dp, 50.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .border(
                        BorderStroke(1.dp, Color.LightGray),
                        RoundedCornerShape(4.dp)
                    )
                    .background(Color.White) // 背景色が枠線からはみ出るので背景色のパラメーターはclipとborderの後に設定
                    .clickable { visible = !visible },
                contentAlignment = Alignment.CenterStart,
            ) {

                // 選択中の日付
                val yMd = SimpleDateFormat("y年M月d日", Locale.JAPANESE)
                Text(
                    text = when {
                        viewModel.payDate.toDate("yyyy-MM-dd") == null -> yMd.format(Date())
                        else -> yMd.format(viewModel.payDate.toDate("yyyy-MM-dd")!!)
                    },
                    fontSize = 16.sp,
                    modifier = Modifier.padding(10.dp)
                )

                // 選択アイコン
                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = "選択アイコン",
                    modifier = Modifier.align(Alignment.CenterEnd)
                )

                if (visible) {

                    // デフォルトでdatePickerに表示する日付
                    val setState = viewModel.payDate.let { viewModel.payDate.toDate() } ?: Date()
                    val state = rememberDatePickerState(setState.time)
                    val getDate = state.selectedDateMillis?.let {
                        Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
                    }

                    // 日付選択ダイアログ
                    DatePickerDialog(
                        onDismissRequest = {

                            // 日付選択ダイアログ非表示
                            visible = false
                        },
                        confirmButton = {
                            Row {
                                TextButton(
                                    onClick = {

                                        // 日付選択ダイアログ非表示
                                        visible = false
                                    },
                                    content = { Text(text = "キャンセル") }
                                )
                                TextButton(
                                    onClick = {

                                        // 日付選択ダイアログ非表示
                                        visible = false

                                        // 選択した日付をViewModelに保存
                                        viewModel.payDate =
                                            getDate?.let {
                                                getDate.format(
                                                    DateTimeFormatter.ofPattern("yyyy-MM-dd 12:00:00")
                                                )
                                            }
                                                ?: if (viewModel.payDate == "") {
                                                    LocalDate.now()
                                                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd 12:00:00"))
                                                } else {
                                                    val localDate = LocalDate.parse(
                                                        viewModel.payDate,
                                                        DateTimeFormatter.ofPattern("yyyy-MM-dd 12:00:00")
                                                    )
                                                    localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd 12:00:00"))
                                                }

                                    },
                                    content = { Text(text = "OK") }
                                )
                            }
                        }
                    ) {

                        // DatePicker
                        DatePicker(
                            state = state,
                            showModeToggle = false,
                            dateValidator = {

                                // 未来日と二か月前の月の選択を不可にするためのバリデーション
                                // 課金ユーザーは無制限
                                isRegisteredUserReferenceEditDate(it)
                            }
                        )
                    }
                }
            }

            // バリデーションメッセージ
            if (viewModel.inputValidatePayDateText != "") {
                Text(
                    text = viewModel.inputValidatePayDateText,
                    color = Color.Red,
                    fontSize = 14.sp
                )
            }

            // 金額

            // 入力項目名
            Text(
                text = "金額",
                modifier = Modifier
                    .padding(top = 16.dp)
                    .padding(bottom = 4.dp),
                fontWeight = FontWeight.Bold
            )

            // テキストフィールド 数値のみ
            TextField(
                value = textFieldValue,
                onValueChange = { inputText ->

                    // 半角英数値または12文字以内でしか入力できないようにする
                    if (
                        inputText.text.filter { it in '0'..'9' }.length == inputText.text.length &&
                        inputText.text.length < 13
                    ) viewModel.price = inputText.text
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true, // 改行禁止
                modifier = Modifier
                    .width(320.dp)
                    .focusRequester(focusRequester)
            )

            // フォーカスを当てる
            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }

            // バリデーションメッセージ
            if (viewModel.inputValidatePriceText != "") {
                Text(
                    text = viewModel.inputValidatePriceText,
                    color = Color.Red,
                    fontSize = 14.sp
                )
            }

            // カテゴリー

            // ドロップダウンメニューの表示非表示切り替え
            val expanded = remember { mutableStateOf(false) }

            // セレクトフィールドのデフォルトの選択状態
            val selectOptionText = remember { mutableStateOf("カテゴリーを選択してください") }
            // ドロップダウンメニューに表示するカテゴリー一覧を取得
            val categoryList by viewModel.category.collectAsState(initial = emptyList())
            categoryList.forEach {

                if (viewModel.addCategoryFlg) {
                    // カテゴリー追加画面からの戻り

                    // カテゴリー順の番号と一致したカテゴリーをViewModelに保存しカテゴリー名をセレクトフィールドの表示テキストにする
                    if (it.categoryOrder == viewModel.addCategoryOrder) {

                        // セレクトフィールドの表示テキスト
                        selectOptionText.value = it.categoryName
                        // 一致したカテゴリーIDをViewModelに保存
                        viewModel.categoryId = it.id.toString()
                    }
                } else {

                    // カテゴリーIDと一致したカテゴリー名をセレクトフィールドの表示テキストにする
                    if (it.id.toString() == viewModel.categoryId) {
                        // 編集の場合のみ通る

                        // セレクトフィールドの表示テキスト
                        selectOptionText.value = it.categoryName
                    }
                }
            }

            // 入力項目名
            Text(
                text = "カテゴリー",
                modifier = Modifier
                    .padding(top = 16.dp)
                    .padding(bottom = 4.dp),
                fontWeight = FontWeight.Bold
            )

            // カテゴリー選択フィールド
            Box(
                contentAlignment = Alignment.CenterStart,
                modifier = Modifier
                    .size(320.dp, 50.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .border(
                        BorderStroke(1.dp, Color.LightGray),
                        RoundedCornerShape(4.dp)
                    )
                    .background(Color.White) // 背景色が枠線からはみ出るので背景色のパラメーターはclipとborderの後に設定
                    .clickable { expanded.value = !expanded.value }
            ) {

                // 選択中のカテゴリー名
                Text(text = selectOptionText.value, modifier = Modifier.padding(start = 10.dp))

                // 選択アイコン
                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = "選択アイコン",
                    modifier = Modifier.align(Alignment.CenterEnd)
                )

                // ドロップダウンメニュー
                DropdownMenu(
                    expanded = expanded.value,
                    modifier = Modifier.width(260.dp),
                    onDismissRequest = { expanded.value = false }
                ) {

                    // ドロップダウンに表示するカテゴリーを一覧表示
                    categoryList.forEach {

                        // ドロップダウンに表示するカテゴリー
                        DropdownMenuItem(
                            text = { Text(text = it.categoryName) },
                            onClick = {

                                // 選択したカテゴリー名をセレクトフィールドの表示テキストにする
                                selectOptionText.value = it.categoryName

                                // 選択したカテゴリーをViewModelに保存
                                viewModel.categoryId = it.id.toString()

                                // ドロップダウンメニューの非表示
                                expanded.value = false
                            }
                        )

                        // 区切り線
                        Spacer(
                            modifier = Modifier
                                .height(1.dp)
                                .fillMaxWidth()
                                .background(Color.LightGray)
                        )
                    }

                    // カテゴリー追加ボタン
                    DropdownMenuItem(
                        text = {
                            Row {
                                Icon(
                                    imageVector = Icons.Default.AddCircle,
                                    contentDescription = "カテゴリー追加"
                                )
                                Text(text = "カテゴリー追加", modifier = Modifier.padding(start = 8.dp))
                            }
                        },
                        onClick = {

                            // ドロップダウンメニューの非表示
                            expanded.value = false

                            // カテゴリー追加画面への遷移
                            // 支出登録、編集画面のViewModelを引き継いで遷移

                            // idがnullかnotNullで追加画面からのルーティング、編集画面からのルーティングの判定をする
                            if (id != null) {

                                // 編集画面からのルーティング
                                navController.navigate(ScreenRoute.AddCategoryFromEditExpenditureItem.route + "/${id}")
                            } else {

                                // 追加画面からのルーティング
                                navController.navigate(ScreenRoute.AddCategoryFromAddExpenditureItem.route)
                            }
                        },
                        modifier = Modifier
                            .background(Color.White)
                            .height(56.dp)
                    )

                    // 区切り線
                    Spacer(
                        modifier = Modifier
                            .height(1.dp)
                            .fillMaxWidth()
                            .background(Color.LightGray)
                    )
                }
            }

            // バリデーションメッセージ
            if (viewModel.inputValidateSelectCategoryText != "") {
                Text(
                    text = viewModel.inputValidateSelectCategoryText,
                    color = Color.Red,
                    fontSize = 14.sp
                )
            }

            // 支出内容

            // 入力項目名
            Text(
                text = "支出内容",
                modifier = Modifier
                    .padding(top = 16.dp)
                    .padding(bottom = 4.dp),
                fontWeight = FontWeight.Bold
            )

            // テキストフィールド
            TextField(
                value = viewModel.content,
                onValueChange = {
                    viewModel.content = it
                },
                modifier = Modifier
                    .size(320.dp, 104.dp)
            )

            // バリデーションメッセージ
            if (viewModel.inputValidateContentText != "") {
                Text(
                    text = viewModel.inputValidateContentText,
                    color = Color.Red,
                    fontSize = 14.sp
                )
            }
        }
    }
}