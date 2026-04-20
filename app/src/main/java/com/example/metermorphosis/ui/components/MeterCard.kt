package com.example.metermorphosis.ui.components

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.metermorphosis.data.api.NetworkModule
import com.example.metermorphosis.data.model.MeterResponse
import com.example.metermorphosis.ui.theme.ColorOutlineVariant
import com.example.metermorphosis.ui.theme.ColorPrimary
import com.example.metermorphosis.ui.theme.ColorSecondary
import com.example.metermorphosis.ui.theme.dateFormatter
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MeterCard(
    meter: MeterResponse,
    onClick: () -> Unit,
    token: String
) {
    var lastPhotoBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var photoLoaded by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(meter.id) {
        try {
            val response = NetworkModule.api.getReadings("Bearer $token", meter.id)
            if (response.isSuccessful) {
                val readings = response.body() ?: emptyList()
                val lastPhotoUrl = readings
                    .sortedByDescending { it.createdAt }
                    .firstOrNull { !it.photoUrl.isNullOrEmpty() }
                    ?.photoUrl

                if (lastPhotoUrl != null) {
                    val filename = lastPhotoUrl.substringAfterLast("/")
                    val fileResponse = NetworkModule.api.getFile("Bearer $token", filename)
                    if (fileResponse.isSuccessful) {
                        val bytes = fileResponse.body()?.bytes()
                        lastPhotoBitmap = bytes?.let {
                            BitmapFactory.decodeByteArray(it, 0, it.size)
                        }
                    }
                }
            }
        } catch (e: Exception) {
            // Тихо игнорируем
        } finally {
            photoLoaded = true
        }
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = ColorPrimary), // Основной цвет карточки
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
     ) {
        Column {
            // Верхняя часть: Миниатюра + Текст
            Row(
                modifier = Modifier
                    .padding(top = 11.dp, start = 9.dp, bottom = 0.dp, end = 9.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                // Заглушка для фото (серый прямоугольник)
                Surface(
                    modifier = Modifier.size(60.dp),
                    color = ColorOutlineVariant,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    when {
                        lastPhotoBitmap != null -> {
                            Image(
                                bitmap = lastPhotoBitmap!!.asImageBitmap(),
                                contentDescription = "Последнее фото",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                        !photoLoaded -> {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = ColorPrimary,
                                strokeWidth = 2.dp
                            )
                        }
                        else -> {
                            Icon(
                                Icons.Default.PhotoCamera,
                                contentDescription = null,
                                tint = ColorSecondary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Текстовый блок справа от фото
                Column {
                    Text(
                        text = meter.name.uppercase(),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Описание",
                        color = Color.White,
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.height(5.dp))

                    Text(
                        text = "Кстати, вы прекрасны!", // meter.type,
                        fontSize = 12.sp,
                        color = ColorOutlineVariant,
                    )

                    Spacer(modifier = Modifier.height(5.dp))

                    Text(
                        text = LocalDate.now().format(dateFormatter).toString(), // meter.lastReadingDate ?: "new", // тут должно быть последнее обновление карточки, испаврим позже
                        fontSize = 12.sp,
                        color = Color(0xFFE5E5E5)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}