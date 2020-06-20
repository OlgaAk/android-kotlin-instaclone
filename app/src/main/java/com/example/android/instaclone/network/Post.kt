package com.example.android.instaclone.network

import com.squareup.moshi.Json

data class Post(
        val id: String,
        val alt_description: String?,
        val description: String?,
        val likes: Long,
        @Json(name = "urls") val url: Url
)

data class Url(
        @Json(name = "small")  val imgScrUrl: String
)