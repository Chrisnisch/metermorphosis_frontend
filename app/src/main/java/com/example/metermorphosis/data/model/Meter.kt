package com.example.metermorphosis.data.model

data class MeterResponse(
    val id: Long,
    val name: String,
    val type: String, // "HOT_WATER" или "COLD_WATER"
    val lastReadingValue: Int? = null,
    val lastReadingDate: String? = null,
    val userId: Long
)

data class CreateMeterRequest(
    val name: String,
    val type: String // "HOT_WATER" или "COLD_WATER"
)

data class UpdateMeterRequest(
    val name: String,
    val type: String
)