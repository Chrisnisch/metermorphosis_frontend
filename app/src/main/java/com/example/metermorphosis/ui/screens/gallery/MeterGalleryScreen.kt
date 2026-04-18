//package com.example.metermorphosis.ui.screens.gallery
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.shadow
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.example.metermorphosis.ui.components.CustomBottomMenuItem
//import com.example.metermorphosis.ui.theme.*
//
//@Composable
//fun MeterGalleryScreen(
//    meterId: Long,
//    meterName: String,
//    onBackClick: () -> Unit,
//    onNavigateToStat: () -> Unit
//) {
//
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(ColorBackground)
//    ) {
//        Column(modifier = Modifier.fillMaxSize()) {
//            // 1. ЗАГОЛОВОК (Название счетчика и кнопка назад)
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 24.dp),
//                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Column {
//                    Text(
//                        text = meterName,
//                        fontSize = 32.sp,
//                        fontWeight = FontWeight.Bold,
//                        color = ColorPrimary
//                    )
//                    Text(
//                        text = "Добавленные фотографии",
//                        fontSize = 14.sp,
//                        color = ColorSecondary
//                    )
//                }
//
//                IconButton(onClick = onBackClick) {
//                    Icon(
//                        Icons.Default.Close,
//                        contentDescription = null,
//                        tint = ColorPrimary)
//                }
//            }
//
//            // 2. ОСНОВНОЙ КОНТЕНТ (Белая подложка как на Дашборде)
//            Surface(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(horizontal = 10.dp)
//                    .weight(1f),
//                shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
//                color = ColorPrimaryContainer
//            ) {
//
//            }
//        }
//        // 3. ПАРЯЩЕЕ МЕНЮ (Три пункта: Основные, Статистика, Галерея)
//        Surface(
//            modifier = Modifier
//                .align(Alignment.BottomCenter)
//                .padding(16.dp)
//                .height(70.dp)
//                .shadow(12.dp, RoundedCornerShape(45.dp)),
//            shape = RoundedCornerShape(45.dp),
//            color = ColorPrimaryContainer,
//        ) {
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceEvenly,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
////                CustomBottomMenuItem(
////                    icon = Icons.Default.Info,
////                    label = "Основные",
////                    isSelected = selectedTab == "Основные",
////                    onClick = { selectedTab = "Основные" }
////                )
//                CustomBottomMenuItem(
//                    icon = Icons.Default.BarChart,
//                    label = "Статистика",
//                    isSelected = false,
//                    onClick = { onNavigateToStat() }
//                )
//                CustomBottomMenuItem(
//                    icon = Icons.Default.PhotoLibrary,
//                    label = "Галерея",
//                    isSelected = true,
//                    onClick = { }
//                )
//            }
//        }
//    }
//}


package com.example.metermorphosis.ui.screens.gallery

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.metermorphosis.data.api.NetworkModule
import com.example.metermorphosis.data.model.ReadingResponse
import com.example.metermorphosis.ui.components.CustomBottomMenuItem
import com.example.metermorphosis.ui.theme.*
import com.example.metermorphosis.viewmodel.DetailsViewModel
import java.io.File

@Composable
fun MeterGalleryScreen(
    token: String,
    meterId: Long,
    meterName: String,
    detailsViewModel: DetailsViewModel = viewModel(),
    onNavigateToStatistics: () -> Unit,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val readings by detailsViewModel.readings.collectAsState()
    val isLoading by detailsViewModel.isLoading.collectAsState()
    val recognizedValue by detailsViewModel.recognizedValue.collectAsState()

    var selectedPhotoUri by remember { mutableStateOf<Uri?>(null) }
    var showConfirmDialog by remember { mutableStateOf(false) }
    var cameraUri by remember { mutableStateOf<Uri?>(null) }

    // Лаунчер галереи
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedPhotoUri = it
            detailsViewModel.recognizeFromPhoto(token, context, it)
            showConfirmDialog = true
        }
    }

    // Лаунчер камеры
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && cameraUri != null) {
            selectedPhotoUri = cameraUri
            detailsViewModel.recognizeFromPhoto(token, context, cameraUri!!)
            showConfirmDialog = true
        }
    }

    // Загружаем показания при открытии
    LaunchedEffect(meterId) {
        detailsViewModel.loadReadings(token, meterId)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorBackground)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // 1. ЗАГОЛОВОК
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
            // 2. КОНТЕНТ
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
                    .weight(1f),
                shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
                color = ColorPrimaryContainer
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    // Кнопки добавления фото
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = { galleryLauncher.launch("image/*") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = ColorPrimary)
                        ) {
                            Icon(Icons.Default.PhotoLibrary, contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            Text("Галерея")
                        }

                        OutlinedButton(
                            onClick = {
                                val file = File(
                                    context.cacheDir,
                                    "meter_${System.currentTimeMillis()}.jpg"
                                )
                                cameraUri = FileProvider.getUriForFile(
                                    context, "${context.packageName}.provider", file
                                )
                                cameraLauncher.launch(cameraUri!!)
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Icon(Icons.Default.CameraAlt, contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            Text("Камера")
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    // Список показаний
                    if (isLoading && readings.isEmpty()) {
                        Box(
                            Modifier.fillMaxWidth().weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = ColorPrimary)
                        }
                    } else if (readings.isEmpty()) {
                        Box(
                            Modifier.fillMaxWidth().weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    Icons.Default.PhotoLibrary,
                                    contentDescription = null,
                                    modifier = Modifier.size(64.dp),
                                    tint = ColorSecondary
                                )
                                Spacer(Modifier.height(8.dp))
                                Text("Нет показаний", color = ColorSecondary)
                                Text("Добавьте первое фото!", color = ColorSecondary, fontSize = 12.sp)
                            }
                        }
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            contentPadding = PaddingValues(bottom = 100.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            items(readings) { reading ->
                                ReadingCard(
                                    reading = reading,
                                    onDelete = {
                                        detailsViewModel.deleteReading(token, reading.id, meterId)
                                    }
                                )
                            }
                        }
                    }
                }
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
                    isSelected = false,
                    onClick = { onNavigateToStatistics() }
                )

                CustomBottomMenuItem(
                    icon = Icons.Default.PhotoLibrary,
                    label = "Галерея",
                    isSelected = true,
                    onClick = { }
                )
            }
        }

        // 4. ДИАЛОГ ПОДТВЕРЖДЕНИЯ
        if (showConfirmDialog && selectedPhotoUri != null) {
            ConfirmReadingDialog(
                recognizedValue = recognizedValue,
                isLoading = isLoading,
                onConfirm = { value ->
                    detailsViewModel.createReading(
                        token = token,
                        meterId = meterId,
                        value = value,
                        context = context,
                        photoUri = selectedPhotoUri!!,
                        onSuccess = {
                            showConfirmDialog = false
                            selectedPhotoUri = null
                        }
                    )
                },
                onDismiss = {
                    showConfirmDialog = false
                    selectedPhotoUri = null
                    detailsViewModel.clearRecognizedValue()
                }
            )
        }
    }
}

@Composable
fun ConfirmReadingDialog(
    recognizedValue: Int?,
    isLoading: Boolean,
    onConfirm: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    var manualValue by remember { mutableStateOf(recognizedValue?.toString() ?: "") }

    LaunchedEffect(recognizedValue) {
        recognizedValue?.let { manualValue = it.toString() }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Подтвердите показание") },
        text = {
            Column {
                if (isLoading) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp))
                        Spacer(Modifier.width(12.dp))
                        Text("Распознаём значение...")
                    }
                } else if (recognizedValue != null) {
                    Text("Распознано: $recognizedValue", color = Color(0xFF4CAF50))
                }
                Spacer(Modifier.height(12.dp))
                Text("Введите или скорректируйте:")
                Spacer(Modifier.height(8.dp))
                TextField(
                    value = manualValue,
                    onValueChange = { manualValue = it.filter { ch -> ch.isDigit() } },
                    placeholder = { Text("Например: 12345") },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { manualValue.toIntOrNull()?.let { onConfirm(it) } },
                enabled = manualValue.isNotBlank() && !isLoading
            ) { Text("Сохранить") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Отмена") }
        }
    )
}

@Composable
fun ReadingCard(reading: ReadingResponse, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (!reading.photoUrl.isNullOrEmpty()) {
                val imageUrl = reading.photoUrl?.let { url ->
                    // Убираем дублирование /files/
                    val cleanUrl = url.replace("//files/", "/")
                    when {
                        cleanUrl.startsWith("http") -> cleanUrl
                        cleanUrl.startsWith("/") -> "${NetworkModule.BASE_URL.trimEnd('/')}$cleanUrl"
                        else -> "${NetworkModule.BASE_URL}$cleanUrl"
                    }
                    android.util.Log.d("GALLERY", "FIXED photoUrl=[${cleanUrl}]")
                }

                AsyncImage(
                    model = imageUrl,
                    contentDescription = null,
                    modifier = Modifier.size(60.dp),
                    contentScale = ContentScale.Crop
                )
                Spacer(Modifier.width(12.dp))
            }
            Column(modifier = Modifier.weight(1f)) {
                Text("Показание: ${reading.value}", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(reading.createdAt?.take(10) ?: "—", color = ColorSecondary, fontSize = 12.sp)
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = null, tint = PurpleGrey40)
            }
        }
    }
}