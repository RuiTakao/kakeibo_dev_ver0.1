package com.kakeibo.kakeibo_dev_6.presentation.expenditure_item.expenditure_item_list

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun ExpenditureItemListScreen2(
    navController: NavController,
    startDate: String? = null,
    lastDate: String? = null,
    categoryId: String? = null,
    dateProperty: String? = null,
    viewModel: ExpenditureItemListViewModel = hiltViewModel()
) {
    // クエリ絞り込み用のフォーマット
//    val df = SimpleDateFormat("yyyy-MM-dd", Locale.JAPANESE)
//
//    /*
//    明細遷移時にのみ実行する処理
//    支出一覧からのパラメーターを処理するため
//     */
//    if (viewModel.pageTransitionFlg) {
//
//        // パラメーターから受け取った基準日をViewModelに保存
//        viewModel.standardOfStartDate = startDate?.let {
//            startDate.toDate("yyyy-MM-dd")
//        } ?: viewModel.standardOfStartDate
//
//        // パラメーターから受け取った選択期間をViewModelに保存
//        dateProperty?.let { viewModel.dateProperty = dateProperty }
//        // パラメーターから受け取ったカテゴリーIDをViewModelに保存
//        categoryId?.let { viewModel.selectCategoryId = categoryId.toInt() }
//
//        // 選択期間がカスタムの場合はパラメーターから受け取った開始日、最終日をViewModelに保存
//        if (dateProperty != null && dateProperty == DateProperty.CUSTOM.name) {
//            viewModel.customOfStartDate = startDate?.let {
//                startDate.toDate("yyyy-MM-dd")
//            } ?: viewModel.customOfStartDate
//
//            viewModel.customOfEndDate = lastDate?.let {
//                lastDate.toDate("yyyy-MM-dd")
//            } ?: viewModel.customOfEndDate
//        }
//
//        // 遷移時の処理終了
//        viewModel.pageTransitionFlg = false
//    }
//
//    // ログ確認用に変数に格納
//    val selectStartDate = df.format(viewModel.selectDate(SelectDate.START))
//    val selectLastDate = df.format(viewModel.selectDate(SelectDate.LAST))
//    // 日付が期待通りに絞り込まれているかログで確認
//    Log.d(
//        "明細 支出一覧、日付出力範囲", "$selectStartDate - $selectLastDate"
//    )
//
//    /** DB */
//    // 支出の登録されている日付を取得
//    val expenditureItemListGropeByPayDate by viewModel.expenditureItemListGropeByPayDate(
//        startDate = selectStartDate,
//        endDate = selectLastDate,
//        sortOfPayDate = viewModel.sortOfPayDate,
//        categoryId = viewModel.selectCategoryId
//    ).collectAsState(initial = emptyList())
//
//    /** DB */
//    // 支出一覧をカテゴリーと結合し抽出
//    val expenditureItemList by viewModel.expenditureItemList(
//        startDate = selectStartDate,
//        endDate = selectLastDate,
//        sortOfPayDate = viewModel.sortOfPayDate,
//        categoryId = viewModel.selectCategoryId
//    ).collectAsState(initial = emptyList())
//
//    // 金額合計
//    var totalTax = 0
//    expenditureItemList.forEach {
//        totalTax += it.price.toInt()
//    }
//
//    Scaffold(
//        topBar = {
//            MainTopBar(title = "支出項目 明細", navigation = {
//                IconButton(onClick = { navController.popBackStack() }, content = {
//                    Icon(
//                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
//                        contentDescription = "戻る",
//                        tint = Color(0xFF854A2A)
//                    )
//                })
//            }, actions = {})
//        }, floatingActionButton = {
//            FAButton(onClick = {
//                // 支出追加ページに遷移
//                navController.navigate(ScreenRoute.AddExpenditureItem.route)
//            })
//        },
//        containerColor = Color(Colors.BASE_COLOR),
//        modifier = Modifier.fillMaxSize()
//    ) { paddingValues ->
//        Column(modifier = Modifier.padding(paddingValues)) {
//            DisplaySwitchArea(
//                totalTax = totalTax, viewModel = viewModel, searchArea = true
//            )
//
//            /* 支出リスト */
//            ItemList(
//                parentItem = expenditureItemListGropeByPayDate,
//                childItemList = expenditureItemList,
//                dateProperty = viewModel.dateProperty,
//                clickable = { id ->
//
//                    // 支出詳細ページに遷移
//                    navController.navigate(
//                        route = ScreenRoute.ExpenditureItemDetail.route + "/${id}"
//                    )
//                }
//            )
//        }
//    }
}


//@Composable
//private fun ItemList(
//    parentItem: List<ExpenditureItem>,
//    dateProperty: String,
//    childItemList: List<ExpenditureItemJoinCategory>,
//    clickable: (Int) -> Unit
//) {
//    LazyColumn(
//        modifier = Modifier
//            .padding(top = 16.dp)
//            .padding(bottom = 80.dp)
//    ) {
//
//        // 支出が登録されている支出日をグルーピングして一覧表示
//        items(parentItem) {
//
//            // 支出日を格納
//            val parentPayDate = it.payDate
//            val mf = SimpleDateFormat("M月d日", Locale.JAPANESE)
//
//            if (dateProperty != DateProperty.DAY.name) {
//
//                // 支出日をタイトルとして表示
//                Text(
//                    text = mf.format(it.payDate.toDate("yyyy-MM-dd")!!),
//                    fontSize = 20.sp,
//                    modifier = Modifier
//                        .padding(horizontal = 16.dp)
//                        .padding(bottom = 4.dp)
//                        .padding(top = 16.dp)
//                )
//            } else {
//                Spacer(modifier = Modifier.height(16.dp))
//            }
//
//            Column(
//                modifier = Modifier
//                    .padding(horizontal = 8.dp)
//                    .clip(RoundedCornerShape(5.dp))
//                    .background(Color.White),
//            ) {
//                ChildItemList(
//                    childItemList = childItemList,
//                    parentPayDate = parentPayDate,
//                    clickable = { id -> clickable(id) }
//                )
//            }
//        }
//    }
//}
//
//@Composable
//private fun ChildItemList(
//    childItemList: List<ExpenditureItemJoinCategory>,
//    parentPayDate: String,
//    clickable: (Int) -> Unit
//) {
//    // 区切り線の可否判定（グルーピングしたカラムの２行目に区切り線入れる）
//    var rowCount = 0
//
//    // 支出リスト出力
//    childItemList.forEach {
//
//        // グルーピングした日付以外の支出リストはスキップ
//        if (parentPayDate != it.payDate) {
//            return@forEach
//        }
//
//        // ２行目以降は区切り線入れる
//        if (rowCount > 0) {
//            Spacer(
//                modifier = Modifier
//                    .height(1.dp)
//                    .padding(horizontal = 8.dp)
//                    .background(Color.LightGray)
//                    .fillMaxWidth()
//            )
//        }
//        // 区切り線可否の判定後加算
//        rowCount++
//
//        Row(
//            modifier = Modifier
//                .clickable { clickable(it.id) }
//                .padding(vertical = 8.dp, horizontal = 16.dp)
//                .fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.Top,
//            content = {
//                Column(
//                    content = {
//
//                        // 支出内容
//                        Text(
//                            text = if (it.content != "") it.content else "？",
//                            fontSize = 20.sp,
//                            lineHeight = 0.sp,
//                            maxLines = 1,
//                            overflow = TextOverflow.Ellipsis,
//                            modifier = Modifier.width(200.dp)
//                        )
//
//                        // カテゴリー
//                        Text(
//                            text = it.categoryName,
//                            modifier = Modifier.padding(vertical = 4.dp),
//                            fontSize = 14.sp,
//                            lineHeight = 0.sp
//                        )
//                    }
//                )
//
//                // 金額
//                Text(text = "￥${priceFormat(it.price)}", fontSize = 20.sp)
//            }
//        )
//    }
//}