package com.example.metermorphosis.ui.screens.register

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
fun RegistrationScreen(onNavigateToLogin: () -> Unit) {
    var loginText by remember { mutableStateOf("") }
    var passwordText by remember { mutableStateOf("") }
    var repeatPasswordText by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppHeader()

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "Регистрация",
            fontSize = 36.sp,
            color = ColorPrimary,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Контейнер с 3 полями
        InputContainer(
            loginValue = loginText,
            onLoginChange = {newText -> loginText = newText},
            passwordValue = passwordText,
            onPasswordChange = {newText -> passwordText = newText},
            repeatPasswordValue = repeatPasswordText,
            onRepeatPasswordChange = {newText -> repeatPasswordText = newText},
            isRegistration = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { /* Логика создания */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(104.dp),
            colors = ButtonDefaults.buttonColors(containerColor = ColorPrimary)
        ) {
            Text("Создать аккаунт", color = ColorOnSurface)
        }

        Spacer(modifier = Modifier.height(16.dp))

        BottomNavigationText(
            mainText = "Есть аккаунт? ",
            actionText = "Войти",
            onClick = onNavigateToLogin
        )

        Spacer(modifier = Modifier.weight(1.5f))
    }
}