package com.example.metermorphosis.ui.screens.splash

import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.metermorphosis.R // Твой пакет
import com.example.metermorphosis.ui.theme.ColorPrimary
import com.example.metermorphosis.ui.theme.ColorPrimaryContainer

@Composable
fun SplashScreen() {
    var isVisible by remember { mutableStateOf(false) }

    // Анимация плавного появления логотипа
    LaunchedEffect(Unit) {
        isVisible = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorPrimaryContainer),
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn() + expandIn()
        ) {
            Icon(
                imageVector = Icons.Default.WaterDrop, // Или любая другая
                contentDescription = null,
                modifier = Modifier.size(100.dp),
                tint = ColorPrimary
            )
        }
    }
}