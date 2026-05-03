package com.example.metermorphosis.data.api

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

data class RecognizeResponse(
    val value: Int
)

interface MlApi {
    @Multipart
    @POST("recognize") // Уточни эндпоинт
    suspend fun recognize(
        @Part file: MultipartBody.Part
    ): Response<RecognizeResponse>
}