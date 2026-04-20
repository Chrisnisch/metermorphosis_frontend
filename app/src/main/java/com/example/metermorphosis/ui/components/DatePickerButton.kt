package com.example.metermorphosis.ui.components

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
//import [InternalLinks33]Context
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Calendar

@Composable
fun DatePickerButton(
    selectedDate: String,
    onDateSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // Парсим дату каждый раз заново
    var year: Int
    var month: Int
    var day: Int

    try {
        val parts = selectedDate.take(10).split("-")
        year = parts[0].toInt()
        month = parts[1].toInt() - 1 // Calendar.MONTH начинается с 0
        day = parts[2].toInt()
    } catch (e: Exception) {
        val c = Calendar.getInstance()
        year = c.get(Calendar.YEAR)
        month = c.get(Calendar.MONTH)
        day = c.get(Calendar.DAY_OF_MONTH)
    }

    OutlinedButton(
        onClick = {
            DatePickerDialog(
                context,
                { _, y, m, d ->
                    // Просто дата: 2026-04-20
                    val date = String.format("%04d-%02d-%02d", y, m + 1, d)
                    onDateSelected(date)
                },
                year,
                month,
                day
            ).show()
        },
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Icon(Icons.Default.CalendarMonth, contentDescription = null)
        Spacer(Modifier.width(8.dp))
        Text(
            text = formatDisplayDate(selectedDate),
            fontSize = 16.sp
        )
    }
}

private fun formatDisplayDate(date: String): String {
    return try {
        val clean = date.take(10) // "2026-04-20"
        val parts = clean.split("-")
        val months = listOf(
            "января", "февраля", "марта", "апреля", "мая", "июня",
            "июля", "августа", "сентября", "октября", "ноября", "декабря"
        )
        val d = parts[2].toInt()
        val m = months[parts[1].toInt() - 1]
        val y = parts[0]
        "$d $m $y"
    } catch (e: Exception) {
        date
    }
}