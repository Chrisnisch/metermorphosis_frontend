package com.example.metermorphosis.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkModule {
    const val BASE_URL = "http://10.0.2.2:8080/"
    private const val ML_URL = "http://10.0.2.2:8000/"

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: MeterApi by lazy {
        retrofit.create(MeterApi::class.java)
    }

    // ML API (распознавание)
    val mlApi: MlApi by lazy {
        Retrofit.Builder()
            .baseUrl(ML_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MlApi::class.java)
    }

}