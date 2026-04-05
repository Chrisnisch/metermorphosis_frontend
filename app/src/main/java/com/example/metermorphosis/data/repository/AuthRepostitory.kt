package com.example.metermorphosis.data.repository

import com.example.metermorphosis.data.api.NetworkModule
import com.example.metermorphosis.data.model.AuthResponse
import com.example.metermorphosis.data.model.LoginRequest
import retrofit2.Response

class AuthRepository {
    suspend fun login(username: String, pass: String): Response<AuthResponse> {
        return NetworkModule.api.login(LoginRequest(login = username, password = pass))
    }

    suspend fun register(username: String, pass: String): Response<AuthResponse> {
        return NetworkModule.api.register(LoginRequest(login = username, password = pass))
    }
}