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
import com.example.metermorphosis.data.api.TokenManager
import com.example.metermorphosis.ui.screens.dashboard.DashboardScreen
import com.example.metermorphosis.ui.screens.meterDetails.MeterDetailScreen
import com.example.metermorphosis.ui.screens.gallery.MeterGalleryScreen
import com.example.metermorphosis.ui.screens.login.LoginScreen
import com.example.metermorphosis.ui.screens.register.RegistrationScreen
import com.example.metermorphosis.ui.screens.settings.SettingsScreen
import com.example.metermorphosis.ui.screens.splash.SplashScreen
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
    val context = androidx.compose.ui.platform.LocalContext.current
    var currentMeterId by remember { mutableStateOf<Long?>(null) }
    var currentMeterName by remember { mutableStateOf("") }

    // Создаем ViewModel с параметром (через Factory)
    val authViewModel: AuthViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
        factory = object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                return AuthViewModel(TokenManager(context)) as T
            }
        }
    )

    val token by authViewModel.token.collectAsState() // следим за токеном

    // Простая переменная состояния: true = экран входа, false = экран регистрации
    var currentScreen by remember { mutableStateOf("Splash") }

    // --- ПРОВЕРКА ПРИ ЗАПУСКЕ ---
    LaunchedEffect(Unit) {
        authViewModel.checkExistingToken()
        // Небольшая задержка, чтобы приложение не "прыгало"
        if (authViewModel.token.value != null) {
            currentScreen = "Dashboard"
        } else {
            currentScreen = "Login"
        }
    }

    LaunchedEffect(token) {
        if (token != null) {
            currentScreen = "Dashboard"
        } else if (currentScreen == "Dashboard") {
            currentScreen = "Login"
        } else if (currentScreen != "Splash" && currentScreen != "Register") {
            currentScreen = "Login"
        }
    }

    when (currentScreen) {
        "Splash" -> SplashScreen(

        )
        "Login" -> LoginScreen(
            viewModel = authViewModel,
            onNavigateToRegister = {currentScreen = "Register"}
        )
        "Register" -> RegistrationScreen(
            viewModel = authViewModel,
            onNavigateToLogin = {currentScreen = "Login"}
        )
        "Dashboard" -> DashboardScreen(
            token = token ?: "",
            onNavigateToSettings = { currentScreen = "Settings" },
            onMeterClick = { id, name ->
                currentMeterId = id
                currentMeterName = name
                currentScreen = "MeterDetails"
            }
        )
        "Settings" -> SettingsScreen(
            onNavigateToDashboard = { currentScreen = "Dashboard" }, // Назад по кнопке меню
            onLogoutClick = {
                authViewModel.logout()
                currentScreen = "Login"
            }
        )
        "MeterDetails" -> MeterDetailScreen(
            token = token ?: "",
            meterId = currentMeterId ?: 0L,
            meterName = currentMeterName,
            onBackClick = { currentScreen = "Dashboard" },
            onNavigateToGallery = { currentScreen = "MeterGallery" }
        )
        "MeterGallery" -> MeterGalleryScreen(
            meterId = currentMeterId ?: 0L,
            meterName = currentMeterName,
            onBackClick = { currentScreen = "Dashboard" },
            onNavigateToStat = { currentScreen = "MeterDetails" }
        )
    }
}