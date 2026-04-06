package com.example.metermorphosis.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.metermorphosis.data.api.NetworkModule
import com.example.metermorphosis.data.model.MeterRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailsViewModel: ViewModel() {
    private val _isUpdating = MutableStateFlow(false)
    val isUpdating = _isUpdating.asStateFlow()

    fun updateMeterName(token: String, id: Long, newName: String) {
        viewModelScope.launch {
            _isUpdating.value = true
            try {
                NetworkModule.api.updateMeter("Bearer $token", id, MeterRequest(newName, "COLD"))
                android.util.Log.e("METERS_DEBUG", "Успешно")
            } catch (e: Exception) {
                android.util.Log.e("SOME ERROR", "ОШИБКА: ${e.message}")
            } finally {
                _isUpdating.value = false
            }
        }
    }
}