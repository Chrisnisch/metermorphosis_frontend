package com.example.metermorphosis.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.metermorphosis.data.api.NetworkModule
import com.example.metermorphosis.data.model.Meter
import com.example.metermorphosis.data.model.MeterRequest
import com.example.metermorphosis.data.repository.MeterRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DashboardViewModel : ViewModel() {
    private val repository = MeterRepository()

    // Состояние списка счетчиков
    private val _meters = MutableStateFlow<List<Meter>>(emptyList())
    val meters = _meters.asStateFlow()

    // Состояние загрузки (чтобы показать индикатор)
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun addMeter(token: String, name: String) {
        viewModelScope.launch {
            try {
                // Отправляем запрос (по умолчанию тип COLD, можешь добавить выбор в диалоге)
                val response = repository.createMeter(token, name, "HOT_WATER")

                if (response.isSuccessful) {
                    // Если успешно создали — сразу обновляем список на экране
                    try {
                        loadMeters(token)
                    } catch (e: Exception) {
                        android.util.Log.e("SOME_ERROR", "ОШИБКА: ${e.message}")
                    }
                } else {
                    // Обработка ошибки (например, имя уже занято)
                    val error = response.errorBody()?.string()
                    android.util.Log.e("API_ERROR", "Не удалось создать: $error")
                }
            } catch (e: Exception) {
                android.util.Log.e("API_ERROR", "Ошибка сети: ${e.message}")
            }
        }
    }

    fun loadMeters(token: String) {
        viewModelScope.launch {
            try {
                val response = repository.fetchMeters(token)
                if (response.isSuccessful) {
                    val body = response.body()
                    android.util.Log.d("METERS_DEBUG", "Успех! Пришло: ${body?.size} элементов")
                    _meters.value = body ?: emptyList()
                } else {
                    android.util.Log.e("METERS_DEBUG", "Ошибка сервера: ${response.code()} ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                android.util.Log.e("METERS_DEBUG", "Критическая ошибка: ${e.message}")
            }
        }
    }
}