package com.example.android.instaclone.network

import com.squareup.moshi.Json

data class Post(
        val id: String,
        @Json(name = "alt_description") val description: String,
        val likes: Long,
        val url: Url
)

data class Url(
        @Json(name = "small")  val imgScrUrl: String
)