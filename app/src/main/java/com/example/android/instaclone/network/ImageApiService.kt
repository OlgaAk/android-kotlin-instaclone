package com.example.android.instaclone.network

import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET


/* TODO convert json to object */

private const val BASE_URL =  "https://api.unsplash.com/"

private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()


private val retrofit = Retrofit.Builder()
        //.addConverterFactory(GsonConverterFactory.create()) code without moshi
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(BASE_URL)
        .build()

interface ImageApiService {
    @GET("/photos/?client_id=x6femPyHiRc90WpJsVoA2i-Int-P-5zEOmZOsLcnLPs")
    fun getImages():
            //Call<JsonArray> code without moshi
            Call<List<Post>>
}

object ImageApi{
    val retrofitService : ImageApiService by lazy {
        retrofit.create(ImageApiService::class.java)
    }
}

