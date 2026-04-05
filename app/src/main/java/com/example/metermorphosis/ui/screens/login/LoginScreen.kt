package com.example.metermorphosis.ui.screens.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.metermorphosis.ui.components.AppHeader
import com.example.metermorphosis.ui.components.BottomNavigationText
import com.example.metermorphosis.ui.components.InputContainer
import com.example.metermorphosis.ui.theme.ColorOnSurface
import com.example.metermorphosis.ui.theme.ColorPrimary
import com.example.metermorphosis.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    viewModel: AuthViewModel = viewModel(), // Используем нашу ViewModel
    onNavigateToRegister: () -> Unit
) {
    // Подписываемся на данные из ViewModel (используем by для удобства)
    val loginText by viewModel.loginText.collectAsState()
    val passwordText by viewModel.passwordText.collectAsState()

    // Ошибки полей
    val loginError by viewModel.loginError.collectAsState()
    val passwordError by viewModel.passwordError.collectAsState()

    // Общая ошибка сервера (например, "Неверный пароль")
    val errorMessage by viewModel.errorMessage.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppHeader()

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "Вход",
            fontSize = 36.sp,
            color = ColorPrimary,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Контейнер с полями ввода
        InputContainer(
            loginValue = loginText,
            onLoginChange = { viewModel.loginText.value = it },
            loginError = loginError, // Передаем ошибку в UI

            passwordValue = passwordText,
            onPasswordChange = { viewModel.passwordText.value = it },
            passwordError = passwordError,

            isRegistration = false
        )

        // Показываем ошибку сервера, если она есть (красным текстом)
        if (errorMessage != null) {
            Text(
                text = errorMessage!!,
                color = Color.Red,
                modifier = Modifier.padding(vertical = 8.dp),
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Кнопка входа
        Button(
            onClick = {
                // Пытаемся войти
                viewModel.performLogin()
            },
            enabled = !isLoading, // Блокируем кнопку, пока идет запрос
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(104.dp),
            colors = ButtonDefaults.buttonColors(containerColor = ColorPrimary)
        ) {
            if (isLoading) {
                // Крутилка вместо текста во время загрузки
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                Text("Продолжить", color = ColorOnSurface)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        BottomNavigationText(
            mainText = "Нет аккаунта? ",
            actionText = "Зарегистрируйтесь",
            onClick = onNavigateToRegister
        )

        Spacer(modifier = Modifier.weight(1.5f))
    }
}