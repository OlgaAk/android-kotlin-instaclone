package com.example.android.instaclone.instaclonehome

import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.android.instaclone.R
import com.example.android.instaclone.network.Post
import descriptionFormatted
import java.sql.Time
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?){
    imgUrl?.let {
        var imgUri = it.toUri().buildUpon().scheme("https").build()
        Glide.with(imgView.context)
                .load(imgUri)
                .apply(RequestOptions.centerCropTransform()
                        .placeholder(R.drawable.loading_animation)
                        .error(R.drawable.ic_broken_image_black_24dp))
                .into(imgView)
    }
}

@BindingAdapter("imageFromUrlRounded")
fun bindImageFromUrlRounded(imgView: ImageView, imgUrl: String?){
    imgUrl?.let {
        var imgUri = it.toUri().buildUpon().scheme("https").build()
        Glide.with(imgView.context)
                .load(imgUri)
                .apply(RequestOptions.centerCropTransform().circleCrop()
                        .placeholder(R.drawable.loading_animation)
                        .error(R.drawable.ic_broken_image_black_24dp))
                .into(imgView)
    }
}


@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Post>?) {
    val adapter = recyclerView.adapter as ImagePostAdapter
    adapter.addHeaderAndSubmitList(data)
}


@BindingAdapter("timePosted")
fun bindTimePosted(textView: TextView, createdAt: String){
    val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
    val time = parser.parse(createdAt).time
    val formatter = SimpleDateFormat()
    val timeNow = System.currentTimeMillis()
    val diff = timeNow - time
    val seconds = diff/ 1000
    val minutes = seconds / 60
    val hours = minutes / 60
    val days = hours/ 24
    var timeUntilNow : String
    if(days>0){
        timeUntilNow = timeToString(days, "day")
    }else if (hours>0){
        timeUntilNow = timeToString(hours, "hour")
    } else if (minutes>0){
        timeUntilNow = timeToString(minutes, "minute")
    } else{
        timeUntilNow = timeToString(seconds, "second")
    }

    textView.text = timeUntilNow
}

enum class TimeStringFormats(val day: String, val hour: String, val minute: String, val second: String){
    ONEUNITE("день", "час", "минута", "секунда"),
    TWOTHREEFOURUNITES("дня", "часа", "минуты", "секунды"),
    MULTIPLEUNITES("дней", "часов", "минут", "секунд")
}

fun timeToString(timeUnit: Long, unitType: String): String {
    return when (unitType) {
        "day" -> return when (timeUnit % 100) {
            in 11L..14L -> "$timeUnit ${TimeStringFormats.MULTIPLEUNITES.day} назад"
            else -> return  when (timeUnit % 10) {
            1L -> { "$timeUnit ${TimeStringFormats.ONEUNITE.day} назад"}
            in 2L .. 4L -> "$timeUnit ${TimeStringFormats.TWOTHREEFOURUNITES.day} назад"
            else -> "$timeUnit ${TimeStringFormats.MULTIPLEUNITES.day} назад"
        }}
        "hour" -> return when (timeUnit % 100) {
            in 11L..14L -> "$timeUnit ${TimeStringFormats.MULTIPLEUNITES.hour} назад"
            else -> return  when (timeUnit % 10) {
                1L -> { "$timeUnit ${TimeStringFormats.ONEUNITE.hour} назад"}
                in 2L .. 4L -> "$timeUnit ${TimeStringFormats.TWOTHREEFOURUNITES.hour} назад"
                else -> "$timeUnit ${TimeStringFormats.MULTIPLEUNITES.hour} назад"
            }}
        "minute" -> return when (timeUnit % 100) {
            in 11L..14L -> "$timeUnit ${TimeStringFormats.MULTIPLEUNITES.minute} назад"
            else -> return  when (timeUnit % 10) {
                1L -> { "$timeUnit ${TimeStringFormats.ONEUNITE.minute} назад"}
                in 2L .. 4L -> "$timeUnit ${TimeStringFormats.TWOTHREEFOURUNITES.minute} назад"
                else -> "$timeUnit ${TimeStringFormats.MULTIPLEUNITES.minute} назад"
            }}
        else -> return when (timeUnit % 100) {
            in 11L..14L -> "$timeUnit ${TimeStringFormats.MULTIPLEUNITES.second} назад"
            else -> return  when (timeUnit % 10) {
                1L -> { "$timeUnit ${TimeStringFormats.ONEUNITE.second} назад"}
                in 2L .. 4L -> "$timeUnit ${TimeStringFormats.TWOTHREEFOURUNITES.second} назад"
                else -> "$timeUnit ${TimeStringFormats.MULTIPLEUNITES.second} назад"
            }}
    }
}


