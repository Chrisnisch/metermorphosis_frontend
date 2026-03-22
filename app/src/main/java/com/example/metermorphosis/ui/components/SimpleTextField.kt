package com.example.metermorphosis.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.metermorphosis.ui.theme.ColorOnSurfaceVariant
import com.example.metermorphosis.ui.theme.ColorPrimary
import com.example.metermorphosis.ui.theme.ColorSecondary

@Composable
fun SimpleTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isPassword: Boolean = false
) {
    // Прозрачное текстовое поле без стандартной обводки Material Design
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = placeholder,
                color = ColorSecondary
            )
        },
        visualTransformation = if(isPassword) PasswordVisualTransformation()
        else VisualTransformation.None,
        modifier = Modifier.fillMaxWidth(),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent, // Убираем черту снизу
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        singleLine = true
    )
}

@Composable
fun BottomNavigationText(mainText: String, actionText: String, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = mainText,
            fontSize = 16.sp,
            color = ColorOnSurfaceVariant // Или Secondary по желанию
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = actionText,
            fontSize = 16.sp,
            fontStyle = FontStyle.Italic,
            color = ColorPrimary, // Цвет ссылки
            modifier = Modifier.clickable { onClick() }
        )
    }
}