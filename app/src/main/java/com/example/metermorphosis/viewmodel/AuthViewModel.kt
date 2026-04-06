package com.example.metermorphosis.viewmodel

import com.example.metermorphosis.data.api.TokenManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.metermorphosis.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val tokenManager: TokenManager) : ViewModel() {

    private val repository = AuthRepository()

    // --- СОСТОЯНИЕ ПОЛЕЙ ВВОДА (State) ---
    val loginText = MutableStateFlow("")
    val passwordText = MutableStateFlow("")

    // --- СОСТОЯНИЕ ОШИБОК ВАЛИДАЦИИ ---
    private val _loginError = MutableStateFlow<String?>(null)
    val loginError = _loginError.asStateFlow()

    private val _passwordError = MutableStateFlow<String?>(null)
    val passwordError = _passwordError.asStateFlow()

    // --- СОСТОЯНИЕ СЕТЕВОГО ЗАПРОСА ---
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    // --- РЕЗУЛЬТАТ АВТОРИЗАЦИИ (ТОКЕН) ---
    private val _token = MutableStateFlow<String?>(null)
    val token = _token.asStateFlow()

    val registerLoginText = MutableStateFlow("")
    val registerPasswordText = MutableStateFlow("")
    val registerRepeatPasswordText = MutableStateFlow("")

    private val _registerError = MutableStateFlow<String?>(null)
    val registerError = _registerError.asStateFlow()

    fun validateInputs(): Boolean {
        val password = passwordText.value.trim()
        var isValid = true

        // Проверка пароля
        if (password.isEmpty()) {
            _passwordError.value = "Введите пароль"
            isValid = false
        } else if (password.length < 8) {
            _passwordError.value = "Пароль слишком короткий"
            isValid = false
        } else {
            _passwordError.value = null
        }

        return isValid
    }

    fun performLogin() {
        // Сначала проверяем валидность полей
        if (!validateInputs()) return

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                val response = repository.login(loginText.value.trim(), passwordText.value.trim())

                if (response.isSuccessful && response.body() != null) {
                    // УСПЕХ: Сохраняем токен
                    _token.value = response.body()?.accessToken

                    val newToken = response.body()?.accessToken
                    if (newToken != null) {
                        tokenManager.saveToken(newToken) // СОХРАНЯЕМ В ПАМЯТЬ
                        _token.value = newToken
                    }
                    android.util.Log.d("TOKEN_CHECK", "Получен токен: $newToken")

                } else {
                    // ОШИБКА БЭКЕНДА (например, 401 Unauthorized)
//                    _errorMessage.value = "Неверный логин или пароль"
                    val errorBody = response.errorBody()?.string()
                    android.util.Log.e("API_ERROR", "Code: ${response.code()}, Body: $errorBody")

                    _errorMessage.value = "Ошибка ${response.code()}: $errorBody"
                }
            } catch (e: Exception) {
                // ОШИБКА СЕТИ (сервер выключен или нет интернета)
                _errorMessage.value = "Ошибка соединения: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun checkExistingToken() {
        _token.value = tokenManager.getToken()
    }

    fun validateRegistration(): Boolean {
        val login = registerLoginText.value.trim()
        val pass = registerPasswordText.value.trim()
        val repeat = registerRepeatPasswordText.value.trim()

        if (login.isEmpty() || pass.isEmpty()) {
            _registerError.value = "Заполните все поля"
            return false
        }
        if (pass != repeat) {
            _registerError.value = "Пароли не совпадают"
            return false
        }
        if (pass.length < 8) {
            _registerError.value = "Пароль должен быть от 8 символов"
            return false
        }

        _registerError.value = null
        return true
    }

    // Новое состояние для диалога успеха
    private val _showRegisterSuccess = MutableStateFlow(false)
    val showRegisterSuccess = _showRegisterSuccess.asStateFlow()

    fun performRegister() {
        if (!validateRegistration()) return

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.register(
                    registerLoginText.value.trim(),
                    registerPasswordText.value.trim()
                )

                if (response.isSuccessful) {
                    // 1. Показываем галочку
                    _showRegisterSuccess.value = true

                    // 2. Ждем 2 секунды, чтобы юзер порадовался
                    kotlinx.coroutines.delay(1000)

                    // 3. Сохраняем токен и переходим на Dashboard автоматически
//                    _token.value = response.body()?.accessToken
                    val newToken = response.body()?.accessToken
                    if (newToken != null) {
                        tokenManager.saveToken(newToken) // Сохраняем в память
                        _token.value = newToken          // Обновляем StateFlow -> это триггер для входа!
                    }
                } else {
//                    _registerError.value = "Ошибка сервера"
                    val errorBody = response.errorBody()?.string()
                    android.util.Log.e("API_ERROR", "Code: ${response.code()}, Body: $errorBody")

                    _registerError.value = "Ошибка ${response.code()}: $errorBody"
                }
            } catch (e: Exception) {
                _registerError.value = "Нет связи"
            } finally {
                _isLoading.value = false
                _showRegisterSuccess.value = false // Скрываем диалог для будущего
            }
        }
    }

    // В файле AuthViewModel.kt
    fun logout() {
        viewModelScope.launch {
            // 1. Удаляем токен из SharedPreferences
            tokenManager.clearToken()

            // 2. Очищаем переменную во ViewModel
            _token.value = null

            // 3. Обнуляем поля ввода (чтобы при следующем входе было пусто)
            loginText.value = ""
            passwordText.value = ""
            registerLoginText.value = ""
            registerPasswordText.value = ""
            registerRepeatPasswordText.value = ""

            // Ошибки тоже сбрасываем
            _loginError.value = null
            _passwordError.value = null
            _errorMessage.value = null
        }
    }
}