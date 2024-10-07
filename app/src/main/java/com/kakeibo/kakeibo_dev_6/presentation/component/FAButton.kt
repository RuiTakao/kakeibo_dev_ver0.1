package com.kakeibo.kakeibo_dev_6.presentation.component

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun FAButton(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = onClick,
        containerColor = Color(0xFF854A2A),
        shape = CircleShape,
        elevation = FloatingActionButtonDefaults.elevation(0.dp),
        content = {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "追加",
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
        }
    )
}