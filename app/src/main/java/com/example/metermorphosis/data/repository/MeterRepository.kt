package com.example.metermorphosis.data.repository

import com.example.metermorphosis.data.api.NetworkModule
import com.example.metermorphosis.data.model.Meter
import retrofit2.Response

class MeterRepository {
    suspend fun fetchMeters(token: String): Response<List<Meter>> {
        // Важно: сервер ждет строку вида "Bearer <token>"
        return NetworkModule.api.getMeters("Bearer $token")
    }
}