package com.example.metermorphosis.ui.screens.gallery

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.metermorphosis.data.model.ReadingResponse
import com.example.metermorphosis.ui.components.CustomBottomMenuItem
import com.example.metermorphosis.ui.components.DatePickerButton
import com.example.metermorphosis.ui.theme.*
import com.example.metermorphosis.viewmodel.DetailsViewModel
import java.io.File

@RequiresApi(Build.VERSION_CODES.O)
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

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedPhotoUri = it
            detailsViewModel.recognizeFromPhoto(token, context, it)
            showConfirmDialog = true
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && cameraUri != null) {
            selectedPhotoUri = cameraUri
            detailsViewModel.recognizeFromPhoto(token, context, cameraUri!!)
            showConfirmDialog = true
        }
    }

    LaunchedEffect(meterId) {
        detailsViewModel.loadReadings(token, meterId)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorBackground)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
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
                        text = "Обработанные фотографии",
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
                                    token = token,
                                    detailsViewModel = detailsViewModel,
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

        if (showConfirmDialog && selectedPhotoUri != null) {
            ConfirmReadingDialog(
                recognizedValue = recognizedValue,
                isLoading = isLoading,
                photoUri = selectedPhotoUri!!,
                onConfirm = { value, date ->
                    android.util.Log.d("CREATE", "ДИАЛОГ: value=$value, date=$date")
                    detailsViewModel.createReading(
                        token = token,
                        meterId = meterId,
                        value = value,
                        date = date,
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ConfirmReadingDialog(
    recognizedValue: Int?,
    isLoading: Boolean,
    photoUri: Uri,
    onConfirm: (Int, String) -> Unit,
    onDismiss: () -> Unit
) {
    var manualValue by remember { mutableStateOf(recognizedValue?.toString() ?: "") }
    var date by remember { mutableStateOf(todayIso()) }
    val context = LocalContext.current

    LaunchedEffect(recognizedValue) {
        recognizedValue?.let { manualValue = it.toString() }
    }

    val photoBitmap = remember(photoUri) {
        try {
            val inputStream = context.contentResolver.openInputStream(photoUri)
            val bmp = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()
            bmp
        } catch (e: Exception) {
            null
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Подтвердите показание") },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                if (photoBitmap != null) {
                    Image(
                        bitmap = photoBitmap.asImageBitmap(),
                        contentDescription = "Фото счетчика",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(Modifier.height(12.dp))
                }

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

                Text("Значение:", fontWeight = FontWeight.Medium)
                Spacer(Modifier.height(4.dp))
                TextField(
                    value = manualValue,
                    onValueChange = { manualValue = it.filter { ch -> ch.isDigit() } },
                    placeholder = { Text("Например: 12345") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(12.dp))

                Text("Дата:", fontWeight = FontWeight.Medium)
                Spacer(Modifier.height(4.dp))
                DatePickerButton(
                    selectedDate = date,
                    onDateSelected = { newDate -> date = newDate }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { manualValue.toIntOrNull()?.let { onConfirm(it, date) } },
                enabled = manualValue.isNotBlank() && !isLoading
            ) { Text("Сохранить") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Отмена") }
        }
    )
}

@Composable
fun ReadingCard(
    reading: ReadingResponse,
    token: String,
    detailsViewModel: DetailsViewModel,
    onDelete: () -> Unit
) {
    val photos by detailsViewModel.photos.collectAsState()
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }

    LaunchedEffect(reading.photoUrl) {
        reading.photoUrl?.let { detailsViewModel.loadPhoto(token, it) }
    }

    val filename = reading.photoUrl?.substringAfterLast("/")
    val bitmap = filename?.let { photos[it] }
    val isLoaded = filename != null && photos.containsKey(filename)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { showEditDialog = true }, // Клик на карточку
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Миниатюра
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFEEEEEE)),
                contentAlignment = Alignment.Center
            ) {
                when {
                    bitmap != null -> {
                        Image(
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription = "Фото",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                    !isLoaded -> {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = ColorPrimary,
                            strokeWidth = 2.dp
                        )
                    }
                    else -> {
                        Icon(
                            Icons.Default.BrokenImage,
                            contentDescription = null,
                            tint = ColorSecondary,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Показание: ${reading.value}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = reading.createdAt?.take(10) ?: "—",
                    color = ColorSecondary,
                    fontSize = 12.sp
                )
            }

            IconButton(onClick = { showDeleteDialog = true }) {
                Icon(Icons.Default.Delete, contentDescription = null, tint = Color.Red)
            }
        }
    }

    // Диалог удаления
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Удалить показание?") },
            text = {
                Text("Показание ${reading.value} от ${reading.createdAt?.take(10) ?: "—"} будет удалено безвозвратно.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteDialog = false
                        onDelete()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) { Text("Удалить", color = Color.White) }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) { Text("Отмена") }
            }
        )
    }

    // Диалог редактирования
    if (showEditDialog) {
        EditReadingDialog(
            reading = reading,
            photoBitmap = bitmap,
            onConfirm = { newValue, newDate ->
                detailsViewModel.updateReading(token, reading.id, newValue, newDate, reading.meterId)
                showEditDialog = false
            },
            onDismiss = { showEditDialog = false }
        )
    }
}

@Composable
fun EditReadingDialog(
    reading: ReadingResponse,
    photoBitmap: Bitmap?,
    onConfirm: (Int, String) -> Unit, // Только value
    onDismiss: () -> Unit
) {
    var value by remember { mutableStateOf(reading.value.toString()) }
    var date by remember { mutableStateOf(todayIso2()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Редактировать показание") },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                if (photoBitmap != null) {
                    Image(
                        bitmap = photoBitmap.asImageBitmap(),
                        contentDescription = "Фото счетчика",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(Modifier.height(12.dp))
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFEEEEEE)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.BrokenImage,
                            contentDescription = null,
                            tint = ColorSecondary,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                    Spacer(Modifier.height(12.dp))
                }

                Text("Значение:", fontWeight = FontWeight.Medium)
                Spacer(Modifier.height(4.dp))
                TextField(
                    value = value,
                    onValueChange = { value = it.filter { ch -> ch.isDigit() } },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(12.dp))

                Text("Дата:", fontWeight = FontWeight.Medium)
                Spacer(Modifier.height(4.dp))
                DatePickerButton(
                    selectedDate = date,
                    onDateSelected = { newDate -> date = newDate }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { value.toIntOrNull()?.let { onConfirm(it, date) } },
                enabled = value.isNotBlank()
            ) { Text("Сохранить") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Отмена") }
        }
    )
}

private fun todayIso(): String {
    val c = java.util.Calendar.getInstance()
    return String.format(
        "%04d-%02d-%02d",
        c.get(java.util.Calendar.YEAR),
        c.get(java.util.Calendar.MONTH) + 1,
        c.get(java.util.Calendar.DAY_OF_MONTH)
    )
}

private fun todayIso2(): String {
    val c = java.util.Calendar.getInstance()
    return String.format(
        "%04d-%02d-%02dT00:00:00.000Z",
        c.get(java.util.Calendar.YEAR),
        c.get(java.util.Calendar.MONTH) + 1,
        c.get(java.util.Calendar.DAY_OF_MONTH)
    )
}
