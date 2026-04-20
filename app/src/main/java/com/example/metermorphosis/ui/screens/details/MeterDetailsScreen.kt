package com.example.metermorphosis.ui.screens.details

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.metermorphosis.data.model.ReadingResponse
import com.example.metermorphosis.ui.components.AddPhotoDialog
import com.example.metermorphosis.ui.components.CustomBottomMenuItem
import com.example.metermorphosis.ui.components.StatCard
import com.example.metermorphosis.ui.theme.*
import com.example.metermorphosis.viewmodel.DetailsViewModel
import androidx.compose.ui.text.rememberTextMeasurer

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
    var showPhotoDialog by remember { mutableStateOf(false) }
    val stats by detailsViewModel.stats.collectAsState()
    val readings by detailsViewModel.readings.collectAsState()

    LaunchedEffect(isNewMeter) {
        if (isNewMeter) {
            showPhotoDialog = true
            onNewMeterHandled()
        }
    }

    LaunchedEffect(meterId) {
        detailsViewModel.loadStats(token, meterId)
        detailsViewModel.loadReadings(token, meterId)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorBackground)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // 1. Шапка
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
                        text = "Статистика",
                        fontSize = 14.sp,
                        color = ColorSecondary
                    )
                }
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Default.Close, contentDescription = null, tint = ColorPrimary)
                }
            }

            // 2. Основной контент
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
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    // График
                    Text(
                        text = "Показания за 6 месяцев",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = ColorPrimary
                    )

                    Spacer(Modifier.height(12.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        // Заменяем весь блок графика в Card:

                        Column(modifier = Modifier.padding(16.dp)) {
                            val last6Months = getLast6MonthsData(readings)

                            if (last6Months.isEmpty()) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(180.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        "Недостаточно данных для графика",
                                        color = ColorSecondary,
                                        fontSize = 14.sp
                                    )
                                }
                            } else {
                                SimpleLineChart(
                                    data = last6Months,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(180.dp)
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(20.dp))

                    // Плашки статистики
                    // Плашки статистики
                    Text(
                        text = "Потребление",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = ColorPrimary
                    )

                    Spacer(Modifier.height(8.dp))

                    val currentMonthValue = getCurrentMonthValue(readings)
                    val averageValue = getAverageValue(readings)
                    val sumValue = getSumValue(readings)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StatCard(
                            title = "В этом месяце",
                            value = currentMonthValue?.toString() ?: "—",
                            unit = "м³",
                            icon = Icons.Default.Water,
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            title = "В среднем",
                            value = averageValue?.toString() ?: "—",
                            unit = "м³",
                            icon = Icons.AutoMirrored.Filled.TrendingUp,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(Modifier.height(20.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
//                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StatCard(
                            title = "За все время",
                            value = sumValue?.toString() ?: "—",
                            unit = "м³",
                            icon = Icons.Default.Water,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(Modifier.height(20.dp))

                    // Заглушка ИИ-советов
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F7FF))
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Icon(
                                Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = ColorPrimary,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "ИИ-помощник",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = ColorPrimary
                                )
                                Spacer(Modifier.height(4.dp))
                                Text(
                                    text = "Скоро здесь будут советы от ИИ, продолжайте вести учёт!",
                                    fontSize = 13.sp,
                                    color = ColorSecondary,
                                    lineHeight = 18.sp
                                )
                            }
                        }
                    }

                    // Отступ для парящего меню
                    Spacer(Modifier.height(100.dp))
                }
            }
        }

        // 3. Парящее меню
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
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CustomBottomMenuItem(
                    icon = Icons.Default.BarChart,
                    label = "Статистика",
                    isSelected = true,
                    onClick = { }
                )
                CustomBottomMenuItem(
                    icon = Icons.Default.PhotoLibrary,
                    label = "Галерея",
                    isSelected = false,
                    onClick = { onNavigateToGallery() }
                )
            }
        }

        if (showPhotoDialog) {
            AddPhotoDialog(
                isNew = isNewMeter,
                onDismiss = { showPhotoDialog = false },
                onPickFromGallery = {
                    showPhotoDialog = false
                    onNavigateToGallery()
                },
//                onTakePhoto = {
//                    showPhotoDialog = false
//                    onNavigateToGallery()
//                }
            )
        }
    }
}

// Модель точки графика
data class ChartDataPoint(
    val value: Float,
    val label: String // "Янв", "Фев" и т.д.
)

// Извлекаем данные за последние 6 месяцев
private fun getLast6MonthsData(readings: List<ReadingResponse>): List<ChartDataPoint> {
    if (readings.isEmpty()) return emptyList()

    val months = listOf(
        "Янв", "Фев", "Мар", "Апр", "Май", "Июн",
        "Июл", "Авг", "Сен", "Окт", "Ноя", "Дек"
    )

    // Группируем по "ГГГГ-ММ"
    val grouped = readings
        .filter { !it.createdAt.isNullOrEmpty() }
        .groupBy { it.createdAt!!.take(7) } // "2026-04"
        .mapValues { entry ->
            entry.value.maxOf { it.value } // Берём максимальное показание за месяц
        }
        .toSortedMap()
        .entries
        .toList()
        .takeLast(6) // Последние 6 месяцев

    if (grouped.size < 2) return emptyList()

    return grouped.map { (yearMonth, value) ->
        val monthIndex = yearMonth.substring(5, 7).toIntOrNull()?.minus(1) ?: 0
        ChartDataPoint(
            value = value.toFloat(),
            label = months.getOrElse(monthIndex) { yearMonth }
        )
    }
}

// Простой линейный график
@Composable
private fun SimpleLineChart(
    data: List<ChartDataPoint>,
    modifier: Modifier = Modifier
) {
    val primaryColor = ColorPrimary
    val gridColor = Color(0xFFEEEEEE)
    val dotColor = ColorPrimary
    val fillColor = ColorPrimary.copy(alpha = 0.1f)
    val textColor = ColorSecondary

    val minValue = data.minOf { it.value }
    val maxValue = data.maxOf { it.value }

    // Красивая шкала с равными промежутками
    val rawRange = maxValue - minValue
    val step = calculateNiceStep(rawRange)
    val niceMin = (Math.floor((minValue / step).toDouble()) * step).toInt()
    val niceMax = (Math.ceil((maxValue / step).toDouble()) * step).toInt()
    val niceRange = (niceMax - niceMin).toFloat().coerceAtLeast(1f)
    val tickCount = ((niceMax - niceMin) / step.toInt()).coerceIn(2, 6)

    val smoothness = 0.35f
    val textMeasurer = rememberTextMeasurer()

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height

        val leftPadding = 52f
        val topPadding = 28f
        val bottomPadding = 30f
        val chartWidth = width - leftPadding
        val chartHeight = height - topPadding - bottomPadding

        // Ось Y — сетка и подписи
        for (i in 0..tickCount) {
            val value = niceMin + i * step.toInt()
            val y = topPadding + chartHeight * (1 - (value - niceMin) / niceRange)

            drawLine(
                color = gridColor,
                start = Offset(leftPadding, y),
                end = Offset(width, y),
                strokeWidth = 1f
            )

            val label = value.toString()
            val textLayout = textMeasurer.measure(
                text = label,
                style = androidx.compose.ui.text.TextStyle(
                    fontSize = 10.sp,
                    color = textColor
                )
            )
            drawText(
                textLayoutResult = textLayout,
                topLeft = Offset(
                    x = leftPadding - textLayout.size.width - 8f,
                    y = y - textLayout.size.height / 2f
                )
            )
        }

        if (data.size < 2) return@Canvas

        val stepX = chartWidth / (data.size - 1)

        val points = data.mapIndexed { index, point ->
            val x = leftPadding + index * stepX
            val y = topPadding + chartHeight * (1 - (point.value - niceMin) / niceRange)
            Offset(x, y)
        }

        // Заливка
        val fillPath = Path().apply {
            moveTo(points.first().x, topPadding + chartHeight)
            lineTo(points.first().x, points.first().y)
            for (i in 0 until points.size - 1) {
                val p0 = points[i]
                val p1 = points[i + 1]
                val dx = (p1.x - p0.x) * smoothness
                cubicTo(p0.x + dx, p0.y, p1.x - dx, p1.y, p1.x, p1.y)
            }
            lineTo(points.last().x, topPadding + chartHeight)
            close()
        }
        drawPath(fillPath, fillColor)

        // Линия
        val linePath = Path().apply {
            moveTo(points.first().x, points.first().y)
            for (i in 0 until points.size - 1) {
                val p0 = points[i]
                val p1 = points[i + 1]
                val dx = (p1.x - p0.x) * smoothness
                cubicTo(p0.x + dx, p0.y, p1.x - dx, p1.y, p1.x, p1.y)
            }
        }
        drawPath(
            path = linePath,
            color = primaryColor,
            style = Stroke(width = 3f, cap = StrokeCap.Round)
        )

        // Точки + значения над точками
        points.forEachIndexed { index, point ->
            // Точка
            drawCircle(color = Color.White, radius = 6f, center = point)
            drawCircle(color = dotColor, radius = 4f, center = point)

            // Значение над точкой
            val valueText = data[index].value.toInt().toString()
            val valueLayout = textMeasurer.measure(
                text = valueText,
                style = androidx.compose.ui.text.TextStyle(
                    fontSize = 10.sp,
                    color = primaryColor,
                    fontWeight = FontWeight.Bold
                )
            )
            drawText(
                textLayoutResult = valueLayout,
                topLeft = Offset(
                    x = point.x - valueLayout.size.width / 2f,
                    y = point.y - valueLayout.size.height - 8f
                )
            )

            // Подпись месяца под графиком
            val label = data[index].label
            val labelLayout = textMeasurer.measure(
                text = label,
                style = androidx.compose.ui.text.TextStyle(
                    fontSize = 10.sp,
                    color = textColor
                )
            )
            drawText(
                textLayoutResult = labelLayout,
                topLeft = Offset(
                    x = point.x - labelLayout.size.width / 2f,
                    y = topPadding + chartHeight + 6f
                )
            )
        }
    }
}

// Вычисляем красивый шаг для оси Y
private fun calculateNiceStep(range: Float): Float {
    if (range <= 0f) return 1f
    val roughStep = range / 4f
    val magnitude = Math.pow(10.0, kotlin.math.floor(kotlin.math.log10(roughStep.toDouble()))).toFloat()
    val normalized = roughStep / magnitude

    val niceStep = when {
        normalized <= 1.5f -> 1f
        normalized <= 3.5f -> 2f
        normalized <= 7.5f -> 5f
        else -> 10f
    }
    return niceStep * magnitude
}

// Последнее значение в текущем месяце
private fun getCurrentMonthValue(readings: List<ReadingResponse>): Int? {
    val now = java.util.Calendar.getInstance()
    val currentYearMonth = String.format(
        "%04d-%02d",
        now.get(java.util.Calendar.YEAR),
        now.get(java.util.Calendar.MONTH) + 1
    )

    return readings
        .filter { it.createdAt?.startsWith(currentYearMonth) == true }
        .maxByOrNull { it.createdAt ?: "" }
        ?.value
}

// Среднее из всех показаний
private fun getAverageValue(readings: List<ReadingResponse>): Int? {
    if (readings.isEmpty()) return null
    return readings.map { it.value }.average().toInt()
}

private fun getSumValue(readings: List<ReadingResponse>): Int? {
    if (readings.isEmpty()) return null
    return readings.map { it.value }.sum().toInt()
}