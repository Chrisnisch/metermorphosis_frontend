package com.example.metermorphosis.data.repository

import com.example.metermorphosis.data.api.NetworkModule
import com.example.metermorphosis.data.model.CreateMeterRequest
import com.example.metermorphosis.data.model.MeterResponse
import com.example.metermorphosis.data.model.UpdateMeterRequest
import retrofit2.Response

class MeterRepository {
    suspend fun fetchMeters(token: String): Response<List<MeterResponse>> {
        // Важно: сервер ждет строку вида "Bearer <token>"
        return NetworkModule.api.getMeters("Bearer $token")
    }

    suspend fun createMeter(token: String, name: String, type: String): Response<MeterResponse> {
        val request = CreateMeterRequest(name = name, type = type)
        return NetworkModule.api.createMeter("Bearer $token", request)
    }

    suspend fun updateMeter(token: String, id: Long, name: String, type: String): Response<MeterResponse> {
        val request = UpdateMeterRequest(name = name, type = type)
        return NetworkModule.api.updateMeter("Bearer $token", id, request)
    }
}