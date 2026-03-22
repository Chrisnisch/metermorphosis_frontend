package com.example.metermorphosis.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.metermorphosis.ui.theme.ColorPrimary
import com.example.metermorphosis.ui.theme.ColorSecondary

@Composable
fun AppHeader() {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(10.dp)) {
        // Название приложения
        Text(
            text = "MeterMorphosis",
//            modifier = Modifier.padding(bottom = 10.dp),
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Italic,
            color = ColorPrimary,
            textAlign = TextAlign.Center,
            lineHeight = 44.sp
        )
        // Подзаголовок
        Text(
            text = "Войдите, чтобы использовать приложение",
            fontSize = 14.sp,
            color = ColorSecondary,
            modifier = Modifier.padding(top = 4.dp),
            textDecoration = TextDecoration.Underline
        )
    }
}