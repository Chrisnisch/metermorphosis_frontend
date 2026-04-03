package com.example.metermorphosis.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.metermorphosis.data.repository.AuthRepostitory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val repository = AuthRepostitory()

    // состояние загрузки чтобы крутилку показывать
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    // Храним токен после входа
    private val _token = MutableStateFlow<String?>(null)
    val token = _token.asStateFlow()

    fun performLogin(user: String, pass: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.login(user, pass)
                if (response.isSuccessful) {
                    _token.value = response.body()?.accessToken
                    // Тут можно сохранить токен в SharedPrefs (локально)
                } else {
                    // Ошибка: неверный логин/пароль (код 401 или 403)
                }
            } catch (e: Exception) {
                // Ошибка сети
            } finally {
                _isLoading.value = false
            }
        }
    }
}