package com.example.kakeibo_dev_6.component.page

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.kakeibo_dev_6.component.parts.SubTopBar
import com.example.kakeibo_dev_6.viewModel.ReplaceOrderCategoryViewModel
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable

@Composable
fun ReplaceOrderCategory(
    navController: NavController,
    viewModel: ReplaceOrderCategoryViewModel = hiltViewModel()
) {

    val categories by viewModel.category.collectAsState(initial = emptyList())
    val data = remember { mutableStateOf(categories) }
    LaunchedEffect(categories) {
        data.value = categories
    }
    val state = rememberReorderableLazyListState(onMove = { from, to ->
        data.value = data.value.toMutableList().apply {
            add(to.index, removeAt(from.index))
        }
        viewModel.replaceOrderCategory = data.value
    })

    Scaffold(
        topBar = {
            SubTopBar(
                title = "カテゴリー入替え",
                navigation = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "戻る")
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            viewModel.replaceOrderCategory?.let {
                                var i = viewModel.replaceOrderCategory?.size ?: 0
                                if (i != 0) {
                                    i++
                                    viewModel.replaceOrderCategory?.forEach {
                                        viewModel.categoryOrder = i
                                        viewModel.updateCategory(it)
                                        i--
                                    }
                                }
                            }
                            navController.popBackStack()
                        }
                    ) {
                        Icon(imageVector = Icons.Default.Check, contentDescription = "登録")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .background(Color(0xFFF8F5E3))
        ) {
            LazyColumn(
                state = state.listState,
                modifier = Modifier
                    .reorderable(state)
                    .detectReorderAfterLongPress(state)
                    .padding(top = 24.dp)
                    .fillMaxSize()
            ) {
                items(data.value, { it.id }) { item ->
                    ReorderableItem(
                        reorderableState = state,
                        key = item.id,
                        modifier = Modifier
                            .padding(bottom = 12.dp)
                            .padding(horizontal = 12.dp)
                            .fillMaxWidth()
                    ) { isDragging ->
                        val elevation =
                            animateDpAsState(if (isDragging) 16.dp else 5.dp)
                        Row(
                            modifier = Modifier
                                .shadow(elevation.value)
                                .background(Color.White)
                                .padding(start = 12.dp)
                                .padding(vertical = 12.dp)
                                .fillMaxWidth()
                        ) {
                            Text(text = item.categoryName, fontSize = 20.sp)
                        }
                    }
                }
            }
        }
    }
}