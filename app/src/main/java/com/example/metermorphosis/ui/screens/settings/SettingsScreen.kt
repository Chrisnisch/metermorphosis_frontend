package com.example.metermorphosis.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.metermorphosis.ui.components.CustomBottomMenuItem
import com.example.metermorphosis.ui.components.SettingsItem
import com.example.metermorphosis.ui.theme.ColorBackground
import com.example.metermorphosis.ui.theme.ColorPrimary
import com.example.metermorphosis.ui.theme.ColorPrimaryContainer
import com.example.metermorphosis.ui.theme.ColorSecondary


@Composable
fun SettingsScreen(
    onLogoutClick: () -> Unit,
    onNavigateToDashboard: () -> Unit
) {

    // ИСПОЛЬЗУЕМ BOX, ЧТОБЫ НАКЛАДЫВАТЬ СЛОИ (МЕНЮ ПОВЕРХ КОНТЕНТА)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorBackground)
    ) {
        // --- СЛОЙ 1: ВЕСЬ КОНТЕНТ (ЗАГОЛОВОК + СПИСОК) ---
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {
            // Заголовок
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Текст слева
                Column {
                    Text(
                        text = "Настройки",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = ColorPrimary
                    )
                    Text(
                        text = "Основные настройки приложения",
                        fontSize = 14.sp,
                        color = ColorSecondary
                    )
                }

            }
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
                    .weight(1f),
                shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp), // Закругляем только верхние углы
                color = ColorPrimaryContainer
            ) {
                Column {
                    // Пункт "Выход"
                    SettingsItem(
                        title = "Выйти из аккаунта",
                        icon = Icons.AutoMirrored.Filled.ExitToApp,
                        textColor = Color.Red,
                        onClick = { onLogoutClick() }
                    )
                }
            }
        }
        // --- СЛОЙ 2: ПАРЯЩЕЕ НИЖНЕЕ МЕНЮ (КАСТОМНОЕ) ---
        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
                .height(70.dp)
                .shadow(
                    elevation = 12.dp,
                    shape = RoundedCornerShape(45.dp),
                    spotColor = Color.Black
                ),
            shape = RoundedCornerShape(45.dp),
            color = ColorPrimaryContainer,
        ) {
            // Используем Row вместо NavigationBar для полного контроля
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Кнопка 1: Дашборд (Активная)
                CustomBottomMenuItem(
                    icon = Icons.Default.Home,
                    label = "Дашборд",
                    isSelected = false,
                    onClick = { onNavigateToDashboard() }
                )

                // Кнопка 2: Настройки (Неактивная)
                CustomBottomMenuItem(
                    icon = Icons.Default.Settings,
                    label = "Настройки",
                    isSelected = true,
                    onClick = {  }
                )
            }
        }
    }
}