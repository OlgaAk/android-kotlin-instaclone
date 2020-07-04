package com.example.android.instaclone.instaclonehome

import android.os.Build
import android.text.Html
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.core.text.HtmlCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.android.instaclone.R
import com.example.android.instaclone.network.Post
import descriptionFormatted
import org.w3c.dom.Text
import java.sql.Time
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.max

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
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
fun bindImageFromUrlRounded(imgView: ImageView, imgUrl: String?) {
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

@BindingAdapter("expendableDescription")
fun makeTextExtendable(textView: TextView, post: Post) {
    val textToInsert: Spanned
    val sb = StringBuilder()
    sb.apply {
        append("<b>")
        append(post.user.username)
        append("</b> ")
        append(post.descriptionFinal)
    }
    val max_characters = 110
    val string: String
    if (sb.length < max_characters) {
        string = sb.toString()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textToInsert = Html.fromHtml(string, Html.FROM_HTML_MODE_LEGACY)
        } else {
            textToInsert = HtmlCompat.fromHtml(string, HtmlCompat.FROM_HTML_MODE_LEGACY)
        }
        textView.text = textToInsert
    } else {
        string = "${sb.substring(0, max_characters + 1).toString()}... еще"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textToInsert = Html.fromHtml(string, Html.FROM_HTML_MODE_LEGACY)
        } else {
            textToInsert = HtmlCompat.fromHtml(string, HtmlCompat.FROM_HTML_MODE_LEGACY)
        }
        val spannableString = SpannableString(textToInsert)
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                val tv = view as TextView
                tv.text = post.descriptionFinal
            }
        }
        val startIndex = spannableString.length-3
        val endIndex = spannableString.length
        spannableString.setSpan(clickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.text = spannableString
        textView.movementMethod= LinkMovementMethod.getInstance()
    }


}


@BindingAdapter("timePosted")
fun bindTimePosted(textView: TextView, createdAt: String) {
    val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
    val time = parser.parse(createdAt).time
    val formatter = SimpleDateFormat()
    val timeNow = System.currentTimeMillis()
    val diff = timeNow - time
    val seconds = diff / 1000
    val minutes = seconds / 60
    val hours = minutes / 60
    val days = hours / 24
    var timeUntilNow: String
    if (days > 0) {
        timeUntilNow = declineTimeUnit(days, DAY_DECLINATIONS)
    } else if (hours > 0) {
        timeUntilNow = declineTimeUnit(hours, HOUR_DECLINATIONS)
    } else if (minutes > 0) {
        timeUntilNow =declineTimeUnit(minutes, MINUTE_DECLINATIONS)
    } else {
        timeUntilNow = declineTimeUnit(seconds, SECOND_DECLINATIONS)
    }
    textView.text = timeUntilNow
}

val SECOND_DECLINATIONS = listOf<String>("секунда", "секунды", "секунд")
val MINUTE_DECLINATIONS = listOf<String>("минута", "минуты", "минут")
val HOUR_DECLINATIONS = listOf<String>("час", "часа", "часов")
val DAY_DECLINATIONS = listOf<String>("день","дня","деней")

val ONEUNIT = 0
val TWOTHREEFOURUNITES = 1
val MULTIPLEUNITES = 2

fun declineTimeUnit(timeUnit: Long, declinationsList: List<String>) : String{
    return when (timeUnit % 100) {
        in 11L..14L -> "$timeUnit ${declinationsList[MULTIPLEUNITES]} назад"
        else -> return when (timeUnit % 10) {
            1L -> {
                "$timeUnit ${declinationsList[ONEUNIT]} назад"
            }
            in 2L..4L -> "$timeUnit ${declinationsList[TWOTHREEFOURUNITES]} назад"
            else -> "$timeUnit ${declinationsList[MULTIPLEUNITES]} назад"
        }
    }
}


