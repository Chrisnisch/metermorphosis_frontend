package com.example.metermorphosis.data.api

import com.example.metermorphosis.data.model.*
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface MeterApi {

    // === AUTH ===
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @POST("auth/register")
    suspend fun register(@Body request: LoginRequest): Response<AuthResponse>

    // === METERS ===
    @GET("meters")
    suspend fun getMeters(
        @Header("Authorization") token: String
    ): Response<List<MeterResponse>>

    @POST("meters")
    suspend fun createMeter(
        @Header("Authorization") token: String,
        @Body request: CreateMeterRequest
    ): Response<MeterResponse>

    @PATCH("meters/{id}")
    suspend fun updateMeter(
        @Header("Authorization") token: String,
        @Path("id") id: Long,
        @Body request: UpdateMeterRequest
    ): Response<MeterResponse>

    // === READINGS ===
    @GET("readings")
    suspend fun getReadings(
        @Header("Authorization") token: String,
        @Query("meterId") meterId: Long
    ): Response<List<ReadingResponse>>

    // Создание показания С ФОТО (multipart)
    @Multipart
    @POST("readings")
    suspend fun createReading(
        @Header("Authorization") token: String,
        @Query("meterId") meterId: Long,
        @Query("value") value: Int,
        @Part file: MultipartBody.Part
    ): Response<Reading>

    @DELETE("readings/{id}")
    suspend fun deleteReading(
        @Header("Authorization") token: String,
        @Path("id") id: Long
    ): Response<Unit>

    @PATCH("readings/{id}")
    suspend fun updateReading(
        @Header("Authorization") token: String,
        @Path("id") id: Long,
        @Body request: UpdateReadingRequest
    ): Response<Reading>

    // === RECOGNIZE ===
    @Multipart
    @POST("recognize")
    suspend fun recognizeReading(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part
    ): Response<Map<String, Int>>

    // === FILES ===
    @GET("readings/files/{filename}")
    suspend fun getFile(
        @Header("Authorization") token: String,
        @Path("filename") filename: String
    ): Response<ResponseBody>

    // === STATS ===
    @GET("meters/{meterId}/stats")
    suspend fun getStats(
        @Header("Authorization") token: String,
        @Path("meterId") meterId: Long
    ): Response<StatsResponse>

    // === CHARTS ===
    @GET("meters/{meterId}/chart/readings")
    suspend fun getReadingChart(
        @Header("Authorization") token: String,
        @Path("meterId") meterId: Long,
        @Query("period") period: String = "MONTH"
    ): Response<ChartResponse>

    @GET("meters/{meterId}/chart/consumption")
    suspend fun getConsumptionChart(
        @Header("Authorization") token: String,
        @Path("meterId") meterId: Long,
        @Query("period") period: String = "MONTH"
    ): Response<ChartResponse>
}