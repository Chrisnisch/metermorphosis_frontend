package com.example.metermorphosis.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.metermorphosis.data.api.NetworkModule
import com.example.metermorphosis.data.model.ChartPoint
import com.example.metermorphosis.data.model.MeterResponse
import com.example.metermorphosis.data.model.ReadingResponse
import com.example.metermorphosis.data.model.StatsResponse
import com.example.metermorphosis.data.model.UpdateMeterRequest
import com.example.metermorphosis.data.model.UpdateReadingRequest
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

    private val _photos = MutableStateFlow<Map<String, Bitmap?>>(emptyMap())
    val photos = _photos.asStateFlow()

    private val _stats = MutableStateFlow<StatsResponse?>(null)
    val stats = _stats.asStateFlow()

    private val _chartData = MutableStateFlow<List<ChartPoint>>(emptyList())
    val chartData = _chartData.asStateFlow()

    private val _chartPeriod = MutableStateFlow("MONTH")
    val chartPeriod = _chartPeriod.asStateFlow()

    fun loadStats(token: String, meterId: Long) {
        viewModelScope.launch {
            try {
                val response = NetworkModule.api.getStats("Bearer $token", meterId)
                if (response.isSuccessful) {
                    _stats.value = response.body()
                }
            } catch (e: Exception) {
                android.util.Log.e("STATS", "Ошибка: ${e.message}")
            }
        }
    }

    fun loadChart(token: String, meterId: Long, period: String = "MONTH") {
        _chartPeriod.value = period
        viewModelScope.launch {
            try {
                val response = NetworkModule.api.getReadingChart("Bearer $token", meterId, period)
                if (response.isSuccessful) {
                    _chartData.value = response.body()?.points ?: emptyList()
                }
            } catch (e: Exception) {
                android.util.Log.e("CHART", "Ошибка: ${e.message}")
            }
        }
    }

    fun loadPhoto(token: String, photoUrl: String) {
        val filename = photoUrl.substringAfterLast("/")

        if (_photos.value.containsKey(filename)) return

        viewModelScope.launch {
            try {
                val response = NetworkModule.api.getFile("Bearer $token", filename)
                if (response.isSuccessful) {
                    val bytes = response.body()?.bytes()
                    val bitmap = bytes?.let {
                        BitmapFactory.decodeByteArray(it, 0, it.size)
                    }
                    // bitmap может быть null если файл не картинка
                    _photos.value = _photos.value + (filename to bitmap)
                } else {
                    // Файл не найден (404) — запоминаем null, чтобы не грузить повторно
                    _photos.value = _photos.value + (filename to null)
                }
            } catch (e: Exception) {
                _photos.value = _photos.value + (filename to null)
            }
        }
    }

    // Загрузка истории показаний
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
                    calculateMonthlyStats(list) // Считаем статистику
                }
            } catch (e: Exception) {
                _error.value = "Ошибка загрузки: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    //  Распознавание значения с фото
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

    // Создание показания с фото
    fun createReading(
        token: String,
        meterId: Long,
        value: Int,
        date: String?,
        context: Context,
        photoUri: Uri,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                android.util.Log.d("CREATE", "=== СОЗДАНИЕ ===")
                android.util.Log.d("CREATE", "meterId=$meterId, value=$value, date=$date")
                android.util.Log.d("CREATE", "photoUri=$photoUri")

                val filePart = uriToMultipart(context, photoUri)

                android.util.Log.d("CREATE", "filePart создан: ${filePart.body.contentType()}")

                val response = NetworkModule.api.createReading(
                    token = "Bearer $token",
                    meterId = meterId,
                    value = value,
                    createdAt = date,
                    file = filePart
                )

                android.util.Log.d("CREATE", "code=${response.code()}")
                android.util.Log.d("CREATE", "body=${response.body()}")
                android.util.Log.d("CREATE", "error=${response.errorBody()?.string()}")

                if (response.isSuccessful) {
                    android.util.Log.d("CREATE", "УСПЕХ!")
                    loadReadings(token, meterId)
                    _recognizedValue.value = null
                    onSuccess()
                } else {
                    _error.value = "Ошибка: ${response.errorBody()?.string()}"
                }
            } catch (e: Exception) {
                android.util.Log.e("CREATE", "EXCEPTION: ${e.message}", e)
                _error.value = "Ошибка сети: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Удаление показания
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

    fun updateReading(token: String, readingId: Long, newValue: Int, newDate: String?, meterId: Long) {
        viewModelScope.launch {
            try {
                val response = NetworkModule.api.updateReading(
                    token = "Bearer $token",
                    id = readingId,
                    value = newValue,
                    date = newDate
                )
                if (response.isSuccessful) {
                    loadReadings(token, meterId)
                } else {
                    _error.value = "Ошибка: ${response.errorBody()?.string()}"
                    android.util.Log.e("UPDATE_DEBUG", "WTF: ${_error.value}")
                }
            } catch (e: Exception) {
                _error.value = "Ошибка сети: ${e.message}"
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

    fun recognizePhoto(context: Context, photoUri: Uri) {
        viewModelScope.launch {
            _isLoading.value = true
            _recognizedValue.value = null
            try {
                val filePart = uriToMultipart(context, photoUri)

                android.util.Log.d("ML", "Отправляем фото на распознавание...")

                val response = NetworkModule.mlApi.recognize(file = filePart)

                android.util.Log.d("ML", "code=${response.code()}")
                android.util.Log.d("ML", "body=${response.body()}")

                if (response.isSuccessful) {
                    _recognizedValue.value = response.body()?.value
                    android.util.Log.d("ML", "Распознано: ${response.body()?.value}")
                } else {
                    android.util.Log.e("ML", "Ошибка: ${response.errorBody()?.string()}")
                    _recognizedValue.value = null
                }
            } catch (e: Exception) {
                android.util.Log.e("ML", "EXCEPTION: ${e.message}", e)
                _recognizedValue.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }
}

data class MonthlyPoint(
    val label: String,    // "Янв", "Фев" и т.д.
    val value: Float
)

private val _monthlyData = MutableStateFlow<List<MonthlyPoint>>(emptyList())
val monthlyData = _monthlyData.asStateFlow()

fun calculateMonthlyStats(readings: List<ReadingResponse>) {
    try {
        val now = java.util.Calendar.getInstance()
        val sixMonthsAgo = java.util.Calendar.getInstance().apply {
            add(java.util.Calendar.MONTH, -6)
        }

        val monthNames = listOf(
            "Янв", "Фев", "Мар", "Апр", "Май", "Июн",
            "Июл", "Авг", "Сен", "Окт", "Ноя", "Дек"
        )

        // Группируем показания по месяцам
        val grouped = readings
            .filter { it.createdAt != null }
            .mapNotNull { reading ->
                try {
                    val datePart = reading.createdAt!!.take(10) // "2026-04-20"
                    val parts = datePart.split("-")
                    val year = parts[0].toInt()
                    val month = parts[1].toInt()
                    Triple(year, month, reading.value)
                } catch (e: Exception) {
                    null
                }
            }
            .filter { (year, month, _) ->
                val cal = java.util.Calendar.getInstance().apply {
                    set(java.util.Calendar.YEAR, year)
                    set(java.util.Calendar.MONTH, month - 1)
                }
                cal.after(sixMonthsAgo)
            }
            .groupBy { (year, month, _) -> "$year-$month" }

        // Берём максимальное значение за каждый месяц
        val points = mutableListOf<MonthlyPoint>()
        for (i in 5 downTo 0) {
            val cal = java.util.Calendar.getInstance().apply {
                add(java.util.Calendar.MONTH, -i)
            }
            val year = cal.get(java.util.Calendar.YEAR)
            val month = cal.get(java.util.Calendar.MONTH) + 1
            val key = "$year-$month"
            val maxValue = grouped[key]?.maxOfOrNull { it.third }?.toFloat() ?: 0f
            val label = monthNames[month - 1]
            points.add(MonthlyPoint(label, maxValue))
        }

        _monthlyData.value = points
    } catch (e: Exception) {
        android.util.Log.e("STATS", "Ошибка расчёта: ${e.message}")
    }
}

