package com.example.metermorphosis.data.model

data class StatsResponse(
    val monthConsumption: Int?,
    val averageMonthConsumption: Int?
)

data class ChartPoint(
    val date: String,
    val value: Int
)

data class ChartResponse(
    val points: List<ChartPoint>
)