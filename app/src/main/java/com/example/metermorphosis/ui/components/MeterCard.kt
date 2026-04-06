package com.example.metermorphosis.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.metermorphosis.data.model.Meter
import com.example.metermorphosis.ui.theme.ColorOutlineVariant
import com.example.metermorphosis.ui.theme.ColorPrimary

@Composable
fun MeterCard(meter: Meter) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = ColorPrimary), // Основной цвет карточки
        modifier = Modifier.fillMaxWidth()
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
                    Icon(
                        imageVector = Icons.Default.Info, // Временная иконка
                        contentDescription = null,
                        modifier = Modifier.padding(12.dp),
                        tint = ColorPrimary
                    )
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
                        text = meter.type,
                        fontSize = 12.sp,
                        color = ColorOutlineVariant,
                    )

                    Spacer(modifier = Modifier.height(5.dp))

                    Text(
                        text = meter.lastReadingDate, // тут должно быть последнее обновление карточки, испаврим позже
                        fontSize = 12.sp,
                        color = Color(0xFFE5E5E5)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            HorizontalDivider(color = Color.White, thickness = 1.dp)

            // Нижняя часть: Кнопки с обводкой
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
            ) {
                // Кнопка "Добавить фото"
                TextButton(
                    onClick = { },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = androidx.compose.ui.graphics.RectangleShape
                ) {
                    Text("Добавить фото", color = Color.White, fontSize = 16.sp)
                }

                VerticalDivider(
                    color = Color.White,
                    thickness = 1.dp,
                    modifier = Modifier.fillMaxHeight().width(1.dp)
                )

                // Кнопка "Детали"
                TextButton(
                    onClick = { },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = androidx.compose.ui.graphics.RectangleShape
                ) {
                    Text("Подробнее", color = Color.White, fontSize = 16.sp)
                }
            }
        }
    }
}