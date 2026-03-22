package com.example.metermorphosis

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.metermorphosis.ui.screens.dashboard.DashboardScreen
import com.example.metermorphosis.ui.screens.login.LoginScreen
import com.example.metermorphosis.ui.screens.register.RegistrationScreen
import com.example.metermorphosis.ui.theme.ColorBackground

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Устанавливаем общий фон приложения
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                containerColor = ColorBackground
            ) { innerPadding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    MeterMorphosisApp()
                }
            }
        }
    }
}

// --- 2. ЛОГИКА ПЕРЕКЛЮЧЕНИЯ ЭКРАНОВ ---
@Composable
fun MeterMorphosisApp() {
    // Простая переменная состояния: true = экран входа, false = экран регистрации
    var currentScreen by remember { mutableStateOf("Login") }

    when (currentScreen) {
        "Login" -> LoginScreen(
            onNavigateToRegister = {currentScreen = "Register"},
            onLoginSuccess = {currentScreen = "Dashboard"}
        )
        "Register" -> RegistrationScreen(
            onNavigateToLogin = {currentScreen = "Login"}
        )
        "Dashboard" -> DashboardScreen(
            onNavigateBack = {currentScreen = "Login"}
        )
    }
}