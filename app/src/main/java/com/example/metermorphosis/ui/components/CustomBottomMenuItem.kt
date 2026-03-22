package com.example.metermorphosis.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.metermorphosis.ui.theme.ColorOnPrimaryContainer
import com.example.metermorphosis.ui.theme.ColorPrimary
import com.example.metermorphosis.ui.theme.ColorSecondary

@Composable
fun CustomBottomMenuItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val contentColor = if (isSelected) ColorOnPrimaryContainer else ColorSecondary
    val backgroundColor = if (isSelected) ColorPrimary.copy(alpha = 0.1f) else Color.Transparent

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(100.dp)) // Скругление клика
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 8.dp), // Регулируй ширину зоны клика здесь
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Контейнер иконки (можно добавить фон для активной, если нужно)
        Box(
            contentAlignment = Alignment.Center,
//            modifier = Modifier.background(backgroundColor, RoundedCornerShape(16.dp)).padding(horizontal = 16.dp, vertical = 2.dp) // Раскомментируй, если хочешь "капсулу" вокруг иконки
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(32.dp) // <-- Размер иконки (сделай 32.dp если хочешь еще больше)
            )
        }

        Spacer(modifier = Modifier.height(4.dp)) // Отступ между иконкой и текстом

        Text(
            text = label,
            fontSize = 14.sp,
            color = contentColor,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}