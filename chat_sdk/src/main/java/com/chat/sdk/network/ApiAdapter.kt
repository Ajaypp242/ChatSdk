package com.chat.sdk.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


internal object ApiAdapter {
    private val logging =  HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    private val okHttpClient: OkHttpClient = OkHttpClient.Builder().addInterceptor(logging).build()

    val apiClient: ApiClient = Retrofit.Builder()
        .baseUrl(BaseUrl.ApiUrl)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiClient::class.java)

    val apiClientDev: ApiClient = Retrofit.Builder()
        .baseUrl(BaseUrl.DevApiUrl)
        .client(OkHttpClient())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiClient::class.java)
}