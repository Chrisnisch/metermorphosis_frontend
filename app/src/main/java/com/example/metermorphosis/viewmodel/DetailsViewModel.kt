package com.example.metermorphosis.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.metermorphosis.data.api.NetworkModule
import com.example.metermorphosis.data.model.MeterResponse
import com.example.metermorphosis.data.model.ReadingResponse
import com.example.metermorphosis.data.model.UpdateMeterRequest
import com.example.metermorphosis.data.repository.MeterRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class DetailsViewModel: ViewModel() {
    private val _isUpdating = MutableStateFlow(false)
    val isUpdating = _isUpdating.asStateFlow()
    private val repository = MeterRepository()
    private val _meters = MutableStateFlow<List<MeterResponse>>(emptyList())

    private val _readings = MutableStateFlow<List<ReadingResponse>>(emptyList())
    val readings = _readings.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _recognizedValue = MutableStateFlow<Int?>(null)
    val recognizedValue = _recognizedValue.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()


    // 1. Загрузка истории показаний
    fun loadReadings(token: String, meterId: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = NetworkModule.api.getReadings("Bearer $token", meterId)
                if (response.isSuccessful) {
                    val list = response.body() ?: emptyList()
                    list.forEach{ reading ->
                        android.util.Log.d("GALLERY", "RAW photoUrl=[${reading.photoUrl}]")
                    }
                    _readings.value = response.body()
                        ?.sortedByDescending { it.createdAt } ?: emptyList()
                }
            } catch (e: Exception) {
                _error.value = "Ошибка загрузки: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // 2. Распознавание значения с фото
    fun recognizeFromPhoto(token: String, context: Context, uri: Uri) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val filePart = uriToMultipart(context, uri)
                val response = NetworkModule.api.recognizeReading("Bearer $token", filePart)
                if (response.isSuccessful) {
                    // API возвращает Map<String, Int>, берем первое значение
                    val value = response.body()?.values?.firstOrNull()
                    _recognizedValue.value = value
                }
            } catch (e: Exception) {
                _error.value = "Ошибка распознавания: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // 3. Создание показания с фото
    fun createReading(token: String, meterId: Long, value: Int, context: Context, photoUri: Uri, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val filePart = uriToMultipart(context, photoUri)
                val response = NetworkModule.api.createReading(
                    "Bearer $token", meterId, value, filePart
                )
                if (response.isSuccessful) {
                    loadReadings(token, meterId) // Обновляем список
                    _recognizedValue.value = null // Сбрасываем распознанное значение
                    onSuccess()
                } else {
                    val errorBody = response.errorBody()?.string()
                    _error.value = "Ошибка: $errorBody"
                }
            } catch (e: Exception) {
                _error.value = "Ошибка сети: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // 4. Удаление показания
    fun deleteReading(token: String, readingId: Long, meterId: Long) {
        viewModelScope.launch {
            try {
                val response = NetworkModule.api.deleteReading("Bearer $token", readingId)
                if (response.isSuccessful) {
                    loadReadings(token, meterId)
                }
            } catch (e: Exception) {
                _error.value = "Ошибка удаления"
            }
        }
    }

    // Сброс распознанного значения
    fun clearRecognizedValue() {
        _recognizedValue.value = null
    }

    // Утилита: конвертация Uri в MultipartBody.Part
    private fun uriToMultipart(context: Context, uri: Uri): MultipartBody.Part {
        val inputStream = context.contentResolver.openInputStream(uri)
            ?: throw Exception("Не удалось открыть файл")
        val bytes = inputStream.readBytes()
        inputStream.close()

        val requestBody = bytes.toRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("file", "photo.jpg", requestBody)
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

    fun updateMeterName(token: String, id: Long, newName: String) {
        viewModelScope.launch {
            try {
                // Отправляем запрос на обновление. Тип оставляем COLD или получаем текущий.
                val response = NetworkModule.api.updateMeter(
                    "Bearer $token",
                    id,
                    UpdateMeterRequest(name = newName, type = "COLD")
                )
                if (response.isSuccessful) {
                    loadMeters(token) // Обновляем данные, чтобы новое имя появилось везде
                    Log.e("API", "Successfully updated meter name")
                }
            } catch (e: Exception) {
                Log.e("API", "Error renaming: ${e.message}")
            }
        }
    }
}