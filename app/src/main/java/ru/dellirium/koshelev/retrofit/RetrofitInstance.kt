package ru.dellirium.koshelev.retrofit

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val BASE_URL = "https://developerslife.ru/"

    private val okHttpClient = OkHttpClient.Builder().build()

    private val retrofitClient = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val query: Queries
    get() = retrofitClient.create(Queries::class.java)
}