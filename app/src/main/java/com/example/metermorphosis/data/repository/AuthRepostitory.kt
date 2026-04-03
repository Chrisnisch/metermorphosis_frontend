package com.example.metermorphosis.data.repository

import com.example.metermorphosis.data.api.NetworkModule
import com.example.metermorphosis.data.model.AuthResponse
import com.example.metermorphosis.data.model.LoginRequest
import retrofit2.Response

class AuthRepostitory {
    suspend fun login(username: String, pass: String): Response<AuthResponse> {
        return NetworkModule.api.login(LoginRequest(username, pass))
    }
}