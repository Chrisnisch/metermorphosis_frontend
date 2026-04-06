package com.example.metermorphosis.data.model

data class Meter(
    val id: Long,
    val name: String,
    val type: String,
    val createdAt: String? = null,
    val lastReadingDate: String? = null
)

data class MeterRequest(
    val name: String,
    val type: String
)