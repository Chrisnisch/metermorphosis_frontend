package com.example.metermorphosis.ui.screens.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.metermorphosis.ui.components.AddPhotoDialog
import com.example.metermorphosis.ui.components.CustomBottomMenuItem
import com.example.metermorphosis.ui.theme.*
import com.example.metermorphosis.viewmodel.DetailsViewModel

@Composable
fun MeterDetailScreen(
    meterId: Long,
    meterName: String,
    token: String,
    detailsViewModel: DetailsViewModel = viewModel(),
    onBackClick: () -> Unit,
    onNavigateToGallery: () -> Unit,
    isNewMeter: Boolean = false,
    onNewMeterHandled: () -> Unit
) {
    var showEditDialog by remember { mutableStateOf(false) }
    var showPhotoDialog by remember { mutableStateOf(false) }

    LaunchedEffect(isNewMeter) {
        if (isNewMeter) {
            showPhotoDialog = true
            onNewMeterHandled()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorBackground)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // 1. ЗАГОЛОВОК (Название счетчика и кнопка назад)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = meterName,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = ColorPrimary
                    )
                    Text(
                        text = "Статистика за все время",
                        fontSize = 14.sp,
                        color = ColorSecondary
                    )
                }

                IconButton(onClick = onBackClick) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = null,
                        tint = ColorPrimary)
                }
            }

            // 2. ОСНОВНОЙ КОНТЕНТ (Белая подложка как на Дашборде)
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
                    .weight(1f),
                shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
                color = ColorPrimaryContainer
            ) {
                // TODO
            }
        }
        // 3. ПАРЯЩЕЕ МЕНЮ
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
                CustomBottomMenuItem(
                    icon = Icons.Default.BarChart,
                    label = "Статистика",
                    isSelected = true,
                    onClick = { /* Навигация */ }
                )

                CustomBottomMenuItem(
                    icon = Icons.Default.PhotoLibrary,
                    label = "Галерея",
                    isSelected = false,
                    onClick = { onNavigateToGallery() }
                )
            }
        }

        // Диалог добавления фото
        if (showPhotoDialog) {
            AddPhotoDialog(
                isNew = isNewMeter,
                onDismiss = { showPhotoDialog = false },
                onPickFromGallery = {
                    showPhotoDialog = false
                    onNavigateToGallery() // Переключаем на вкладку Галерея
                    // TODO: Запуск выбора фото (сделаем в следующем шаге)
                },
                onTakePhoto = {
                    showPhotoDialog = false
                    onNavigateToGallery()
                    // TODO: Запуск камеры (сделаем в следующем шаге)
                }
            )
        }

//        if (showEditDialog) {
//            EditNameDialog(
//                currentName = meterName,
//                onDismiss = { showEditDialog = false },
//                onConfirm = { newName ->
//                    detailsViewModel.updateMeterName(token, meterId, newName)
//                    showEditDialog = false
//                }
//            )
//        }
    }
}