package com.example.kakeibo_dev_6.component.parts

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun FAButton(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = onClick,
        containerColor = Color(0xFF854A2A)
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "追加",
            tint = Color.White,
            modifier = Modifier.size(32.dp)
        )
    }
}