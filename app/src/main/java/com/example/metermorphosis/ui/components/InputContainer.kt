package com.example.metermorphosis.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.metermorphosis.ui.theme.ColorOutlineVariant
import com.example.metermorphosis.ui.theme.ColorPrimaryContainer

@Composable
fun InputContainer(
    loginValue: String,
    onLoginChange: (String) -> Unit,
    passwordValue: String,
    onPasswordChange: (String) -> Unit,
    repeatPasswordValue: String = "",
    onRepeatPasswordChange: (String) -> Unit = {},
    isRegistration: Boolean,
    loginError: String? = null,
    passwordError: String? = null
) {
    // Карточка с закругленными углами 19 и цветом Primary Container
    Card(
        shape = RoundedCornerShape(19.dp),
        colors = CardDefaults.cardColors(containerColor = ColorPrimaryContainer),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            // Поле Логин
            SimpleTextField(
                value = loginValue,
                onValueChange = onLoginChange,
                placeholder = "Логин",
                isPassword = false,
                errorMessage = loginError
            )

            // Разделитель (тонкая линия)
            HorizontalDivider(color = ColorOutlineVariant)

            // Поле Пароль
            SimpleTextField(
                value = passwordValue,
                onValueChange = onPasswordChange,
                placeholder = "Пароль",
                isPassword = true,
                errorMessage = passwordError
            )

            // Третье поле, если это регистрация
            if (isRegistration) {
                HorizontalDivider(color = ColorOutlineVariant)
                SimpleTextField(
                    value = repeatPasswordValue,
                    onValueChange = onRepeatPasswordChange,
                    placeholder = "Повторите пароль",
                    isPassword = true
                )
            }
        }
    }
}