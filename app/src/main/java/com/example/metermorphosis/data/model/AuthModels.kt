package com.example.metermorphosis.data.model

data class LoginRequest(
    val username: String,
    val password: String
)

data class AuthResponse(
    val accessToken: String,
    val refreshToken: String
)