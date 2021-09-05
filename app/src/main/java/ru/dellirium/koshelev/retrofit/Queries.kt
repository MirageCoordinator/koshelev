package ru.dellirium.koshelev.retrofit

import retrofit2.Response
import retrofit2.http.GET

interface Queries {

    @GET("random?json=true")
    suspend fun getRandomQuote(): Response<RandomQuote>

}