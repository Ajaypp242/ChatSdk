package com.chat.chat_sdk.network

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object ApiAdapter {
    private val logging =  HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    private val okHttpClient: OkHttpClient = OkHttpClient.Builder().addInterceptor(logging).build()

    val apiClient: ApiClient = Retrofit.Builder()
        .baseUrl("https://s01.live2support.com/dashboardv2/chatwindow/")
//        .baseUrl("https://dev.live2support.com/dashboardv2/chatwindow/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiClient::class.java)
}