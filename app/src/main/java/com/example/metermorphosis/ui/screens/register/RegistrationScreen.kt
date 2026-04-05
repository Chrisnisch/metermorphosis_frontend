package com.example.metermorphosis.ui.screens.register

import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.metermorphosis.ui.components.AppHeader
import com.example.metermorphosis.ui.components.BottomNavigationText
import com.example.metermorphosis.ui.components.InputContainer
import com.example.metermorphosis.ui.components.RegistrationSuccessDialog
import com.example.metermorphosis.ui.theme.ColorOnSurface
import com.example.metermorphosis.ui.theme.ColorPrimary
import com.example.metermorphosis.viewmodel.AuthViewModel

@Composable
fun RegistrationScreen(
    viewModel: AuthViewModel = viewModel(),
    onNavigateToLogin: () -> Unit
) {
    val login by viewModel.registerLoginText.collectAsState()
    val password by viewModel.registerPasswordText.collectAsState()
    val repeatPassword by viewModel.registerRepeatPasswordText.collectAsState()

    val error by viewModel.registerError.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    val showSuccess by viewModel.showRegisterSuccess.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AppHeader()
            Spacer(modifier = Modifier.weight(1f))

            Text("Регистрация", fontSize = 32.sp, color = ColorPrimary)

            Spacer(modifier = Modifier.height(16.dp))

            // Твой InputContainer, настроенный на 3 поля
            InputContainer(
                loginValue = login,
                onLoginChange = { viewModel.registerLoginText.value = it },
                passwordValue = password,
                onPasswordChange = { viewModel.registerPasswordText.value = it },
                repeatPasswordValue = repeatPassword,
                onRepeatPasswordChange = { viewModel.registerRepeatPasswordText.value = it },
                isRegistration = true
            )

            // Отображение ошибки (несовпадение паролей или ошибка сервера)
            if (error != null) {
                Text(
                    text = error!!,
                    color = Color.Red,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { viewModel.performRegister() },
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(100.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ColorPrimary)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
                } else {
                    Text("Создать аккаунт", color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            BottomNavigationText(
                mainText = "Уже есть аккаунт?",
                actionText = "Войдите",
                onClick = onNavigateToLogin
            )
            Spacer(modifier = Modifier.weight(1.5f))
        }

        RegistrationSuccessDialog(isVisible = showSuccess)
    }
}