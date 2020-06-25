package com.example.android.instaclone.network

import com.squareup.moshi.Json
import descriptionFormatted

data class Post(
        val id: String,
        val alt_description: String? = "нет описания",
        val description: String?,
        val likes: Long,
        @Json(name = "urls") val url: Url,
        val user: User
){
    var likesString = "Нравится: ${likes.toString()}"
    val descriptionFormatedString = descriptionFormatted(user.username, description, alt_description)
}

data class Url(
        @Json(name = "small")  val imgScrUrl: String
)

data class User (
        val username: String
)