package com.example.android.instaclone.network

import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET


private const val BASE_URL =  "https://api.unsplash.com/"



private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()

interface ImageApiService {
    @GET("/photos/?client_id=x6femPyHiRc90WpJsVoA2i-Int-P-5zEOmZOsLcnLPs")
    fun getImages():
            Call<JsonArray>
}

object ImageApi{
    val retrofitService : ImageApiService by lazy {
        retrofit.create(ImageApiService::class.java)
    }
}

