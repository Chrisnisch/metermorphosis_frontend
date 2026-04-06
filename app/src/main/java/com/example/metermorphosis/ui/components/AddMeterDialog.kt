package com.example.metermorphosis.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AddMeterDialog(
    onDismiss: () -> Unit,
    onConfirm: (name: String) -> Unit
) {
    // Состояния полей внутри диалога
    var meterName by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Новый счетчик",
                style = MaterialTheme.typography.headlineSmall,
                color = Color(0xFF1D1B20) // Твой ColorPrimary
            )
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Введите название",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Поле ввода названия
                OutlinedTextField(
                    value = meterName,
                    onValueChange = { meterName = it },
                    label = { Text("Название (напр. Кухня)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF6750A4), // Твой ColorPrimary
                        cursorColor = Color(0xFF6750A4)
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (meterName.isNotBlank()) {
                        onConfirm(meterName)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6750A4))
            ) {
                Text("Добавить", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена", color = Color(0xFF6750A4))
            }
        }
    )
}