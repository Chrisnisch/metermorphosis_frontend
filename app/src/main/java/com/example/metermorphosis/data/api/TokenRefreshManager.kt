//package com.example.metermorphosis.data.api
//
//import android.util.Log
//import com.example.metermorphosis.data.api.TokenManager
//import com.example.metermorphosis.data.model.RefreshRequest
//import kotlinx.coroutines.*
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.asStateFlow
//
//class TokenRefreshManager(
//    private val tokenManager: TokenManager
//) {
//    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
//    private var refreshJob: Job? = null
//
//    private val _token = MutableStateFlow<String?>(null)
//    val token = _token.asStateFlow()
//
//    private val _isLoggedOut = MutableStateFlow(false)
//    val isLoggedOut = _isLoggedOut.asStateFlow()
//
//    fun start(accessToken: String, refreshToken: String) {
//        _token.value = accessToken
//        _isLoggedOut.value = false
//
//        tokenManager.saveToken(accessToken)
//        tokenManager.saveRefreshToken(refreshToken)
//        NetworkModule.currentToken = accessToken
//
//        startAutoRefresh()
//    }
//
//    fun restore() {
//        val access = tokenManager.getToken()
//        val refresh = tokenManager.getRefreshToken()
//
//        if (access != null && refresh != null) {
//            _token.value = access
//            NetworkModule.currentToken = access
//            // Сразу обновляем при запуске
//            refreshNow()
//            startAutoRefresh()
//        }
//    }
//
//    private fun startAutoRefresh() {
//        refreshJob?.cancel()
//        refreshJob = scope.launch {
//            while (isActive) {
//                delay(15 * 60 * 1000L) // 15 минут
//                refreshNow()
//            }
//        }
//    }
//
//    private fun refreshNow() {
//        scope.launch {
//            try {
//                val refreshToken = tokenManager.getRefreshToken() ?: return@launch
//
//                Log.d("TOKEN", "Обновляем токен...")
//
//                val response = NetworkModule.api.refreshToken(
//                    RefreshRequest(refreshToken)
//                )
//
//                if (response.isSuccessful) {
//                    val newToken = response.body()?.accessToken
//                    if (newToken != null) {
//                        _token.value = newToken
//                        tokenManager.saveToken(newToken)
//                        NetworkModule.currentToken = newToken
//                        Log.d("TOKEN", "Токен обновлён!")
//                    }
//                } else {
//                    Log.e("TOKEN", "Ошибка обновления: ${response.code()}")
//                    if (response.code() == 401 || response.code() == 403) {
//                        // Refresh token истёк — разлогиниваем
//                        logout()
//                    }
//                }
//            } catch (e: Exception) {
//                Log.e("TOKEN", "Ошибка сети: ${e.message}")
//            }
//        }
//    }
//
//    fun logout() {
//        refreshJob?.cancel()
//        _token.value = null
//        _isLoggedOut.value = true
//        tokenManager.clearAll()
//        NetworkModule.currentToken = null
//    }
//
//    fun destroy() {
//        refreshJob?.cancel()
//        scope.cancel()
//    }
//}