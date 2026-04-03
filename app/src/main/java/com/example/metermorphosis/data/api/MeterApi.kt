package com.example.metermorphosis.data.api

import com.example.metermorphosis.data.model.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface MeterApi {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @POST("auth/register")
    suspend fun register(@Body request: LoginRequest): Response<AuthResponse>
}