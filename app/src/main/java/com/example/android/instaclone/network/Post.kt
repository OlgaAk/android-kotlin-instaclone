package com.example.android.instaclone.network

import com.squareup.moshi.Json
import descriptionFormatted

data class Post(
        val id: String,
        val alt_description: String? = "нет описания",
        val description: String?,
        var likes: Long,
        @Json(name = "urls") val url: Url,
        val user: User,
        val created_at: String,
        var liked_by_user: Boolean
){

    val descriptionFinal = description ?: alt_description
    var bookMarked : Boolean = false
}

data class Url(
        @Json(name = "small")  val imgScrUrl: String
)

data class User (
        val username: String
)