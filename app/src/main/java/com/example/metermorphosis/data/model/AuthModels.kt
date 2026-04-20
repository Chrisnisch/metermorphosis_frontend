package com.example.metermorphosis.data.model

// === АВТОРИЗАЦИЯ ===
data class LoginRequest(
    val login: String,
    val password: String
)

data class RegisterRequest(
    val login: String,
    val password: String
)

data class AuthResponse(
    val accessToken: String,
    val refreshToken: String
)

data class RefreshRequest(
    val refreshToken: String
)

data class RefreshResponse(
    val accessToken: String,
    val refreshToken: String
)