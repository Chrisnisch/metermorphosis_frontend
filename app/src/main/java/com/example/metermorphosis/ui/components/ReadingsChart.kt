package com.example.metermorphosis.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.metermorphosis.data.model.ChartPoint
import com.example.metermorphosis.ui.theme.ColorPrimary
import com.example.metermorphosis.ui.theme.ColorSecondary

@Composable
fun ReadingsChart(
    points: List<ChartPoint>,
    modifier: Modifier = Modifier
) {
    if (points.isEmpty()) {
        Box(modifier = modifier, contentAlignment = Alignment.Center) {
            Text("Нет данных для графика", color = ColorSecondary)
        }
        return
    }

    val maxValue = points.maxOf { it.value }.toFloat().coerceAtLeast(1f)
    val primaryColor = ColorPrimary
    val secondaryColor = ColorSecondary

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val padding = 40f
        val chartWidth = width - padding * 2
        val chartHeight = height - padding * 2
        val barWidth = (chartWidth / points.size) * 0.6f
        val gap = (chartWidth / points.size) * 0.4f

        // Горизонтальные линии сетки
        for (i in 0..4) {
            val y = padding + chartHeight * (1 - i / 4f)
            drawLine(
                color = Color(0xFFEEEEEE),
                start = Offset(padding, y),
                end = Offset(width - padding, y),
                strokeWidth = 1f
            )
        }

        // Столбцы
        points.forEachIndexed { index, point ->
            val barHeight = (point.value / maxValue) * chartHeight
            val x = padding + index * (barWidth + gap) + gap / 2

            // Столбец
            drawRoundRect(
                color = primaryColor,
                topLeft = Offset(x, padding + chartHeight - barHeight),
                size = Size(barWidth, barHeight),
                cornerRadius = CornerRadius(8f, 8f)
            )
        }
    }

    // Подписи под столбцами
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 40.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        points.forEach { point ->
            Text(
                text = formatDate(point.date),
                fontSize = 9.sp,
                color = secondaryColor,
                maxLines = 1
            )
        }
    }
}

private fun formatDate(date: String): String {
    return try {
        // "2024-01-15" -> "Янв"
        val parts = date.split("-")
        val month = parts.getOrNull(1)?.toIntOrNull() ?: return date
        listOf(
            "Янв", "Фев", "Мар", "Апр", "Май", "Июн",
            "Июл", "Авг", "Сен", "Окт", "Ноя", "Дек"
        ).getOrElse(month - 1) { date }
    } catch (e: Exception) {
        date.takeLast(5)
    }
}