package com.example.metermorphosis.ui.screens.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.metermorphosis.ui.components.AppHeader
import com.example.metermorphosis.ui.components.BottomNavigationText
import com.example.metermorphosis.ui.components.InputContainer
import com.example.metermorphosis.ui.theme.ColorOnSurface
import com.example.metermorphosis.ui.theme.ColorPrimary

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    var loginText by remember { mutableStateOf("") }
    var passwordText by remember { mutableStateOf("") }
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally // Центрируем всё по горизонтали
    ) {
        // Заголовок и подзаголовок (общие для обоих экранов)
        AppHeader()

        Spacer(modifier = Modifier.weight(1f)) // Пружинка, толкающая контент к центру

        // Центральный блок
        Text(
            text = "Вход",
            fontSize = 36.sp,
            color = ColorPrimary,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Контейнер с полями ввода (Прямоугольник радиус 19, делится на два поля)
        InputContainer(
            loginValue = loginText,
            onLoginChange = {newText -> loginText = newText},
            passwordValue = passwordText,
            onPasswordChange = {newText -> passwordText = newText},
            isRegistration = false
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Кнопка "Продолжить"
        Button(
            onClick = {
                // идентификация и аутентификация
                onLoginSuccess()
            },
            modifier = Modifier.fillMaxWidth().height(50.dp), // Высота для красоты
            shape = RoundedCornerShape(104.dp), // Радиус 104
            colors = ButtonDefaults.buttonColors(containerColor = ColorPrimary)
        ) {
            Text("Продолжить", color = ColorOnSurface)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Текст внизу "Нет аккаунта? Зарегистрируйтесь"
        BottomNavigationText(
            mainText = "Нет аккаунта? ",
            actionText = "Зарегистрируйтесь",
            onClick = onNavigateToRegister
        )

        Spacer(modifier = Modifier.weight(1.5f)) // Балансировка отступа снизу
    }
}