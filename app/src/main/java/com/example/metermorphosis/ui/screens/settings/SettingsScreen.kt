package com.example.metermorphosis.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
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
import com.example.metermorphosis.ui.components.MeterCard
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
            .background(ColorBackground) // Белый фон всего экрана
    ) {
        // --- СЛОЙ 1: ВЕСЬ КОНТЕНТ (ЗАГОЛОВОК + СПИСОК) ---
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top // Прижимаем всё к верху!
        ) {
            // Заголовок
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 24.dp), // Отступ сверху побольше (под статус бар)
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically // Выравнивание по вертикали по центру строки
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

//                // Кнопка справа
//                Button(
//                    onClick = { /* Логика добавления */ },
//                    shape = androidx.compose.foundation.shape.CircleShape,
//                    colors = ButtonDefaults.buttonColors(containerColor = ColorPrimary),
//                    contentPadding = PaddingValues(0.dp),
//                    modifier = Modifier.size(50.dp)
//                ) {
//                    Icon(
//                        imageVector = Icons.Default.Add,
//                        contentDescription = "Добавить",
//                        tint = Color.White
//                    )
//                }
            }

            // Список настроек
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(19.dp),
                colors = CardDefaults.cardColors(containerColor = ColorPrimaryContainer)
            ) {
                Column {
                    // Пункт "Выход"
                    SettingsItem(
                        title = "Выйти из аккаунта",
                        icon = Icons.Default.ExitToApp,
                        textColor = Color.Red, // Выделим красным для важности
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
                .height(70.dp) // <-- Твоя желаемая высота (можно 65.dp, 60.dp)
                .shadow(
                    elevation = 12.dp,
                    shape = RoundedCornerShape(45.dp),
                    spotColor = Color.Black
                ),
            shape = RoundedCornerShape(45.dp), // Сильное скругление
            color = ColorPrimaryContainer, // Цвет фона меню
        ) {
            // Используем Row вместо NavigationBar для полного контроля
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly, // Равномерное распределение
                verticalAlignment = Alignment.CenterVertically   // Идеальное центрирование по вертикали
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

@Composable
fun SettingsItem(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    textColor: Color = ColorPrimary,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = textColor)
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = title, color = textColor, fontSize = 16.sp)
    }
}