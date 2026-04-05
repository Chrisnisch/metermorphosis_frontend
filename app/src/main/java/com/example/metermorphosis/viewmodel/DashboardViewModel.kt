package com.example.metermorphosis.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.metermorphosis.data.model.Meter
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

//class DashboardViewModel: ViewModel() {
//
//    // Состояние: список счетчиков.
//    // MutableStateFlow — это потокобезопасная переменная, за которой следит Compose
//    private val _meters = MutableStateFlow<List<Meter>>(emptyList())
//    val meters = _meters.asStateFlow() // Открываем наружу только для чтения
//
//
//    fun loadMeters(token: String) {
//        // Имитация загрузки с сервера (позже здесь будет вызов Retrofit)
//        viewModelScope.launch {
//            _meters.value = listOf(
//                Meter(1, "СЧЕТЧИК ВОДЫ", "Кухня, холодная", "01.01.2026"),
//                Meter(2, "ЭЛЕКТРОЭНЕРГИЯ", "Основной щиток", "01.01.2026"),
//                Meter(3, "ГАЗ", "Котельная", "01.01.2026"),
//                Meter(4, "СЧЕТЧИК ВОДЫ", "Ванная, горячая", "01.01.2026"),
//                Meter(5, "счетчик 2", "все плохо", "01.01.2026"),
//                Meter(6, "счетчик 1g-fe", "все очень плохо, жрем 20 литров на 100 км", "01.01.2026")
//            )
//        }
//    }
//}