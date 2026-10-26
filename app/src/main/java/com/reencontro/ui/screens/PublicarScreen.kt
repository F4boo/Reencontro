package com.reencontro.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun PublicarScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE8F2E8)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Publicar",
            color = Color(0xFF245C42),
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
