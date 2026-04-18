package com.example.metermorphosis.data.model

data class ReadingResponse(
    val id: Long,
    val value: Int,
    val photoUrl: String?,
    val createdAt: String?,
    val meterId: Long
)

data class Reading(
    val id: Long,
    val value: Int,
    val photoUrl: String?,
    val createdAt: String?
)