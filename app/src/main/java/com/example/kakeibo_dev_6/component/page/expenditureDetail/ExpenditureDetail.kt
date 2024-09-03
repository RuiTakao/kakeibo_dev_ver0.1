package com.example.kakeibo_dev_6.component.page.expenditureDetail

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.kakeibo_dev_6.MainViewModel
import com.example.kakeibo_dev_6.component.page.expenditureDetail.parts.ListContent
import com.example.kakeibo_dev_6.component.page.expenditureDetail.parts.SearchContext
import com.example.kakeibo_dev_6.component.page.expenditureDetail.parts.SearchDialog
import com.example.kakeibo_dev_6.component.page.expenditureDetail.parts.TopBar
import com.example.kakeibo_dev_6.entity.ExpenditureItemJoinCategory
import java.lang.IllegalArgumentException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun ExpenditureDetail(
    navController: NavController,
    startDate: String? = null,
    lastDate: String? = null,
    categoryId: String? = null,
    dateProperty: String? = null,
    viewModel: MainViewModel = hiltViewModel()
) {
    val df = SimpleDateFormat("yyyy-MM-dd")
    if (viewModel.pageTransitionFlg) {
        viewModel.payDetailStartDate = startDate?.let {
            startDate.toDate("yyyy-MM-dd")
        } ?: viewModel.startDate
        viewModel.payDetailLastDate = lastDate?.let {
            lastDate.toDate("yyyy-MM-dd")
        } ?: viewModel.lastDate

        dateProperty?.let { viewModel.payDetailDateProperty = dateProperty }
        categoryId?.let { viewModel.selectCategory = categoryId.toInt() }
        viewModel.pageTransitionFlg = false
    }
    val categories by viewModel.category.collectAsState(initial = emptyList())
    categories.forEach {
        if (viewModel.selectCategory == it.id) {
            viewModel.selectCategoryName = it.categoryName
        }
    }
    Log.d("prev_db", viewModel.payDetailStartDate.toString())
    val expenditureItemList by viewModel.expenditureItemList(
        firstDay = df.format(viewModel.payDetailStartDate),
        lastDay = df.format(viewModel.payDetailLastDate),
        sort = viewModel.sort,
        categoryId = viewModel.selectCategory
    ).collectAsState(initial = emptyList())

    val isShowSearchDialog = remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopBar(navController = navController)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .background(color = Color(0xFFF7F7F7))
                .fillMaxSize()
        ) {
            SearchContext(
                isShowSearchDialog = isShowSearchDialog,
                expenditureItemList = expenditureItemList,
                viewModel = viewModel
            )
            ListContent(expenditureItemList = expenditureItemList, navController = navController)
        }
        SearchDialog(isShowSearchDialog = isShowSearchDialog, viewModel = viewModel)
    }
}

private fun String.toDate(pattern: String = "yyyy-MM-dd HH:mm:ss"): Date? {
    val format = try {
        SimpleDateFormat(pattern)
    } catch (e: IllegalArgumentException) {
        null
    }
    val date = format?.let {
        try {
            it.parse(this)
        } catch (e: ParseException) {
            null
        }
    }
    return date
}