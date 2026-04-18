package com.example.metermorphosis.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.metermorphosis.ui.theme.ColorPrimary
import com.example.metermorphosis.ui.theme.ColorSecondary

@Composable
fun AddPhotoDialog(
    onDismiss: () -> Unit,
    onPickFromGallery: () -> Unit,
    onTakePhoto: () -> Unit,
    isNew: Boolean = false
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Добавить фотографию", fontWeight = FontWeight.Bold)
        },
        text = {
            if(isNew) {
                Text("Добавьте первое фото счетчика, чтобы начать отслеживать статистику!")
            } else {
                Text("Хотите добавить фото счетчика?")
            }
        },
        confirmButton = {
            Column {
                // Кнопка "Выбрать из галереи"
                Button(
                    onClick = onPickFromGallery,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = ColorPrimary)
                ) {
                    Icon(Icons.Default.PhotoLibrary, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Выбрать из галереи")
                }

                Spacer(Modifier.height(8.dp))

                // Кнопка "Сделать фото"
                OutlinedButton(
                    onClick = onTakePhoto,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.CameraAlt, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Сделать фото")
                }

                Spacer(Modifier.height(8.dp))

                // Кнопка "Пропустить"
                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Позже", color = ColorSecondary)
                }
            }
        },
        // Убираем стандартную dismissButton, мы всё поместили в confirmButton
        dismissButton = null
    )
}