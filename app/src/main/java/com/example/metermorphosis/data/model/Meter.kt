package com.example.metermorphosis.data.model

data class Meter(
    val id: Long,
    val name: String,
    val type: String,
//    val lastReadingValue: Double? = 0.0,
    val createdAt: String? = null,
    val lastReadingDate: String
)
