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

data class MeterResponse(
    val id: Long,
    val name: String,
    val type: String,
    val lastReadingValue: Int? = null,
    val lastReadingDate: String? = null,
)

data class StatsResponse(
    val monthConsumption: Int,
    val averageMonthConsumption: Int
)

data class ChartPoint(
    val date: String,
    val value: Int
)

data class ChartResponse(
    val points: Set<ChartPoint>
)