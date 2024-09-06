package com.example.kakeibo_dev_6.component.page

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.kakeibo_dev_6.component.parts.DisplaySwitchArea
import com.example.kakeibo_dev_6.component.parts.FAButton
import com.example.kakeibo_dev_6.component.parts.MainTopBar
import com.example.kakeibo_dev_6.component.utility.toDate
import com.example.kakeibo_dev_6.entity.ExpenditureItemJoinCategory
import com.example.kakeibo_dev_6.enum.Route
import com.example.kakeibo_dev_6.viewModel.DisplaySwitchAreaViewModel
import com.example.kakeibo_dev_6.viewModel.ExpenditureDetailViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import java.text.SimpleDateFormat

@Composable
fun ExpenditureDetail(
    navController: NavController,
    startDate: String? = null,
    lastDate: String? = null,
    categoryId: String? = null,
    dateProperty: String? = null,
    expenditureDetailViewModel: ExpenditureDetailViewModel = hiltViewModel(),
    displaySwitchAreaViewModel: DisplaySwitchAreaViewModel = hiltViewModel()
) {

    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setStatusBarColor(color = Color(0xFF854A2A))
    }

    val df = SimpleDateFormat("yyyy-MM-dd")
    if (displaySwitchAreaViewModel.pageTransitionFlg) {
        displaySwitchAreaViewModel.startDate = startDate?.let {
            startDate.toDate("yyyy-MM-dd")
        } ?: displaySwitchAreaViewModel.startDate
        displaySwitchAreaViewModel.lastDate = lastDate?.let {
            lastDate.toDate("yyyy-MM-dd")
        } ?: displaySwitchAreaViewModel.lastDate

        dateProperty?.let { displaySwitchAreaViewModel.dateProperty = dateProperty }
        categoryId?.let { displaySwitchAreaViewModel.selectCategory = categoryId.toInt() }
        displaySwitchAreaViewModel.pageTransitionFlg = false
    }
    val categories by displaySwitchAreaViewModel.category.collectAsState(initial = emptyList())
    categories.forEach {
        if (displaySwitchAreaViewModel.selectCategory == it.id) {
            displaySwitchAreaViewModel.selectCategoryName = it.categoryName
        }
    }
    val expenditureItemList by expenditureDetailViewModel.expenditureItemList(
        firstDay = df.format(displaySwitchAreaViewModel.startDate),
        lastDay = df.format(displaySwitchAreaViewModel.lastDate),
        sort = displaySwitchAreaViewModel.sort,
        categoryId = displaySwitchAreaViewModel.selectCategory
    ).collectAsState(initial = emptyList())

    var totalTax = 0
    expenditureItemList.forEach {
        totalTax += it.price.toInt()
    }

    Scaffold(
        topBar = {
            MainTopBar(title = "支出項目 明細", navigation = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "戻る",
                        tint = Color.White
                    )
                }
            }, actions = {})
        },
        floatingActionButton = { FAButton(onClick = { navController.navigate(Route.EDIT_EXPENDITURE.name) }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .background(color = Color(0xFFF8F5E3))
                .fillMaxSize()
        ) {
            DisplaySwitchArea(
                totalTax = totalTax,
                viewModel = displaySwitchAreaViewModel,
                searchArea = true
            )
            LazyColumn {
                items(expenditureItemList) {
                    if (expenditureItemList.indexOf(it) == 0) {
                        Item(
                            expItem = it,
                            navController = navController,
                            titleFlag = true,
                            expenditureDetailViewModel = expenditureDetailViewModel,
                            displaySwitchAreaViewModel = displaySwitchAreaViewModel
                        )
                    } else {
                        if (
                            expenditureItemList.get(expenditureItemList.indexOf(it)).payDate !=
                            expenditureItemList.get(expenditureItemList.indexOf(it) - 1).payDate
                        ) {
                            Item(
                                expItem = it,
                                navController = navController,
                                titleFlag = true,
                                expenditureDetailViewModel = expenditureDetailViewModel,
                                displaySwitchAreaViewModel = displaySwitchAreaViewModel
                            )
                        } else {
                            Item(
                                expItem = it,
                                navController = navController,
                                expenditureDetailViewModel = expenditureDetailViewModel,
                                displaySwitchAreaViewModel = displaySwitchAreaViewModel
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun Item(
    expItem: ExpenditureItemJoinCategory,
    navController: NavController,
    titleFlag: Boolean = false,
    expenditureDetailViewModel: ExpenditureDetailViewModel = hiltViewModel(),
    displaySwitchAreaViewModel: DisplaySwitchAreaViewModel = hiltViewModel()
) {
    if (titleFlag) {
        if (displaySwitchAreaViewModel.dateProperty != "day") {
            val Md = SimpleDateFormat("M月d日")
            Text(
                text = Md.format(expItem.payDate.toDate("yyyy-MM-dd")),
                fontSize = 20.sp,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 4.dp)
                    .padding(top = 32.dp)
            )
        } else {
            Spacer(modifier = Modifier.height(32.dp))
        }
    } else {
        if (displaySwitchAreaViewModel.dateProperty != "day") {
            Spacer(
                modifier = Modifier
                    .height(2.dp)
                    .background(Color.LightGray)
                    .fillMaxWidth()
            )
        } else {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
    Column(
        modifier = Modifier
            .clickable {
                navController.navigate(
                    route = "${Route.EXPENDITURE_ITEM_DETAIL.name}/${expItem.id}"
                )
            }
            .shadow(elevation = 5.dp)
            .background(Color.White)
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column {
                Text(text = expItem.content, fontSize = 20.sp, lineHeight = 0.sp)
                Text(
                    text = expItem.categoryName,
                    modifier = Modifier.padding(vertical = 4.dp),
                    fontSize = 14.sp,
                    lineHeight = 0.sp
                )
            }
            Text(text = "￥${expItem.price}", fontSize = 20.sp)
        }
    }
}