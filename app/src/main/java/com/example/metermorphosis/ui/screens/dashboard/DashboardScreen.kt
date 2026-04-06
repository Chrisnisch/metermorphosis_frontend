package com.example.metermorphosis.ui.screens.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import com.example.metermorphosis.viewmodel.DashboardViewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.metermorphosis.ui.components.AddMeterDialog

@Composable
fun DashboardScreen(
    dashboardViewModel: DashboardViewModel = viewModel(),
    token: String,
    onMeterClick: (Long, String) -> Unit,
    onNavigateToSettings: () -> Unit
) {
    val meters by dashboardViewModel.meters.collectAsState()
    val isLoading by dashboardViewModel.isLoading.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }


    LaunchedEffect(Unit) {
        dashboardViewModel.loadMeters(token)
    }

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
            // 1. ЗАГОЛОВОК И КНОПКА "+"
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
                        text = "Дашборд",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = ColorPrimary
                    )
                    Text(
                        text = "Ваши активные счетчики",
                        fontSize = 14.sp,
                        color = ColorSecondary
                    )
                }

                // Кнопка справа
//                Button(
//                    onClick = { showAddDialog = true },
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

            // 2. КОНТЕЙНЕР С КАРТОЧКАМИ (ВТОРИЧНЫЙ ЦВЕТ)
            // Surface занимает всё оставшееся место (weight 1f)
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
                    .weight(1f),
                shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
                color = ColorPrimaryContainer
            ) {
                if (isLoading && meters.isEmpty()) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(
                            top = 10.dp,
                            start = 10.dp,
                            end = 10.dp,
                            bottom = 100.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        items(meters) { meter ->
                            MeterCard(
                                meter = meter,
                                onClick = { onMeterClick(meter.id, meter.name) }
                            )
                        }
                        item {
                            Button(
                                onClick = { showAddDialog = true },
                                shape = androidx.compose.foundation.shape.CircleShape,
                                colors = ButtonDefaults.buttonColors(containerColor = ColorPrimary),
                                contentPadding = PaddingValues(0.dp),
                                modifier = Modifier.size(50.dp),
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Добавить",
                                    tint = Color.White
                                )
                        }
                        }
                    }
                }
            }
            if (showAddDialog) {
                AddMeterDialog(
                    onDismiss = { showAddDialog = false },
                    onConfirm = { name ->
                        dashboardViewModel.addMeter(token, name)
                        showAddDialog = false
                    }
                )
            }
        }
        // --- СЛОЙ 2: ПАРЯЩЕЕ НИЖНЕЕ МЕНЮ (КАСТОМНОЕ) ---
        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
                .height(70.dp)
                .shadow(elevation = 12.dp, shape = RoundedCornerShape(45.dp), spotColor = Color.Black),
            shape = RoundedCornerShape(45.dp),
            color = ColorPrimaryContainer,
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Кнопка 1: Дашборд (Активная)
                CustomBottomMenuItem(
                    icon = Icons.Default.Home,
                    label = "Дашборд",
                    isSelected = true,
                    onClick = { /* Навигация */ }
                )

                // Кнопка 2: Настройки (Неактивная)
                CustomBottomMenuItem(
                    icon = Icons.Default.Settings,
                    label = "Настройки",
                    isSelected = false,
                    onClick = { onNavigateToSettings() }
                )
            }
        }
    }
}