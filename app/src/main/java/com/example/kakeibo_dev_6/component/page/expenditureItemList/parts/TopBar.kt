package com.example.kakeibo_dev_6.component.page.expenditureItemList.parts

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavController
import com.example.kakeibo_dev_6.MainViewModel
import com.example.kakeibo_dev_6.enum.Route
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    drawerState: DrawerState,
    scope: CoroutineScope,
    navController: NavController,
    viewModel: MainViewModel
) {
    TopAppBar(
        title = {
            Text(
                text = "支出項目",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        navigationIcon = {
            IconButton(
                onClick = {
                    scope.launch {
                        drawerState.apply {
                            if (isClosed) open() else close()
                        }
                    }
                }
            ) {
                Icon(imageVector = Icons.Default.Menu, contentDescription = "メニュー")
            }
        },
        actions = {
            IconButton(onClick = {
                val df = SimpleDateFormat("yyyy-MM-dd")
                val startDate = df.format(viewModel.startDate)
                val lastDate = df.format(viewModel.lastDate)
                navController.navigate("${Route.PAY_DETAIL.name}/0/${viewModel.dateProperty}/${startDate}/${lastDate}")
            }) {
                Icon(imageVector = Icons.Default.ReceiptLong, contentDescription = "詳細")
            }
        }
    )
}