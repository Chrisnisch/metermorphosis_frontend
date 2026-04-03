package com.example.metermorphosis

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.metermorphosis.ui.screens.dashboard.DashboardScreen
import com.example.metermorphosis.ui.screens.login.LoginScreen
import com.example.metermorphosis.ui.screens.register.RegistrationScreen
import com.example.metermorphosis.ui.theme.ColorBackground
import com.example.metermorphosis.viewmodel.AuthViewModel

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
    val authViewModel: AuthViewModel = viewModel()
    val token by authViewModel.token.collectAsState() // следим за токеном

    // Простая переменная состояния: true = экран входа, false = экран регистрации
    var currentScreen by remember { mutableStateOf("Login") }

    LaunchedEffect(token) {
        if (token != null) {
            currentScreen = "Dashboard"
        }
    }

    when (currentScreen) {
        "Login" -> LoginScreen(
            viewModel = authViewModel,
            onNavigateToRegister = {currentScreen = "Register"}
        )
        "Register" -> RegistrationScreen(
            onNavigateToLogin = {currentScreen = "Login"}
        )
        "Dashboard" -> DashboardScreen(
            token = token ?: "",
            onNavigateBack = {currentScreen = "Login"}
        )
    }
}