package com.example.metermorphosis.ui.screens.meterDetails

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
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
    onNavigateToGallery: () -> Unit
) {
    var isEditingName by remember { mutableStateOf(false) }
    var editedName by remember { mutableStateOf(meterName) }

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
                    if (isEditingName) {
                        TextField(
                            value = editedName,
                            onValueChange = { editedName = it },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent
                            ),
                            textStyle = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Bold, color = ColorPrimary),
                            trailingIcon = {
                                IconButton( onClick = {
                                        isEditingName = false
                                        detailsViewModel.updateMeterName(token, meterId, editedName)
                                    }
                                ) {
                                    Icon(Icons.Default.Check, contentDescription = null, tint = ColorOnSurfaceVariant)
                                }
                            }
                        )
                    } else {
                        Text(
                            text = meterName,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = ColorPrimary,
                            modifier = Modifier
                                .clickable { isEditingName = true }
                        )
                    }

                    Text(
                        text = "TYPE",
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

            }
        }
        // 3. ПАРЯЩЕЕ МЕНЮ (Три пункта: Основные, Статистика, Галерея)
        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
                .height(70.dp)
                .shadow(12.dp, RoundedCornerShape(45.dp)),
            shape = RoundedCornerShape(45.dp),
            color = ColorPrimaryContainer,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
//                CustomBottomMenuItem(
//                    icon = Icons.Default.Info,
//                    label = "Основные",
//                    isSelected = selectedTab == "Основные",
//                    onClick = { selectedTab = "Основные" }
//                )
                CustomBottomMenuItem(
                    icon = Icons.Default.BarChart,
                    label = "Статистика",
                    isSelected = true,
                    onClick = {  }
                )
                CustomBottomMenuItem(
                    icon = Icons.Default.PhotoLibrary,
                    label = "Галерея",
                    isSelected = false,
                    onClick = { onNavigateToGallery() }
                )
            }
        }
    }
}