package com.example.android.instaclone.instaclonehome

import android.os.Build
import android.text.Html
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
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
import java.text.SimpleDateFormat

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



// Function to make text expandable on click if it exceeds max

const val MAX_CHARACTERS = 110 // should fit into two lines
const val CLICKABLE_SUBSTRING = "... еще"

@BindingAdapter("expendableDescription")
fun makeTextExtendable(textView: TextView, post: Post) {
    val formattedText = makeSubstringBold(post)
    val textToInsert = convertHtmlToString(formattedText)
    if (textToInsert.length < MAX_CHARACTERS) {
        textView.text = textToInsert
    } else {
        val shortString = shortenString(textToInsert)
        textView.text = makeSubStringClickable(shortString, post.descriptionFinal)
        textView.movementMethod = LinkMovementMethod.getInstance()
    }
}

fun makeSubstringBold(data: Post): String {
    val sb = StringBuilder()
    return sb.apply {
        append("<b>")
        append(data.user.username)
        append("</b> ")
        append(data.descriptionFinal)
    }.toString()
}

fun shortenString(string: Spanned): String {
    return "${string.substring(0, MAX_CHARACTERS + 1)}$CLICKABLE_SUBSTRING"
}

fun convertHtmlToString(string: String): Spanned {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(string, Html.FROM_HTML_MODE_LEGACY)
    } else {
        HtmlCompat.fromHtml(string, HtmlCompat.FROM_HTML_MODE_LEGACY)
    }
}

fun makeSubStringClickable(string: String, expendedText: String?): Spanned {
    val spannableString = SpannableString(string) // needed to make a part of text clickable
    val clickableSpan = object : ClickableSpan() {
        override fun onClick(view: View) {
            val tv = view as TextView // casting view to textview to use its methods
            tv.text = expendedText // expending original text on click
        }

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.isUnderlineText = false
        }
    }
    val startIndex = spannableString.length - CLICKABLE_SUBSTRING.length
    val endIndex = spannableString.length
    spannableString.setSpan(clickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    return spannableString
}



/* Function converts time in yyyy-MM-dd'T'hh:mm:ss'Z' Format to a sentence how long ago it happened */

val SECOND_DECLINATIONS = listOf<String>("секунда", "секунды", "секунд")
val MINUTE_DECLINATIONS = listOf<String>("минута", "минуты", "минут")
val HOUR_DECLINATIONS = listOf<String>("час", "часа", "часов")
val DAY_DECLINATIONS = listOf<String>("день", "дня", "деней")

const val ONEUNIT = 0
const val TWOTHREEFOURUNITES = 1
const val MULTIPLEUNITES = 2

@BindingAdapter("timePosted")
fun bindTimePosted(textView: TextView, createdAt: String) {
    val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
    val time = parser.parse(createdAt).time
    val timeNow = System.currentTimeMillis()
    val diff = timeNow - time
    val seconds = diff / 1000
    val minutes = seconds / 60
    val hours = minutes / 60
    val days = hours / 24
    textView.text =  when {
        days > 0 -> declineTimeUnit(days, DAY_DECLINATIONS)
        hours > 0 -> declineTimeUnit(hours, HOUR_DECLINATIONS)
        minutes > 0 -> declineTimeUnit(minutes, MINUTE_DECLINATIONS)
        else -> declineTimeUnit(seconds, SECOND_DECLINATIONS)
    }
}

fun declineTimeUnit(timeUnit: Long, declinationsList: List<String>): String {
    return when (timeUnit % 100) {
        in 11L..14L -> "$timeUnit ${declinationsList[MULTIPLEUNITES]} назад" // if ends with 11, 12, 13 or 14
        else -> return when (timeUnit % 10) {
            1L -> {
                "$timeUnit ${declinationsList[ONEUNIT]} назад"
            }
            in 2L..4L -> "$timeUnit ${declinationsList[TWOTHREEFOURUNITES]} назад" // if ends with 2,3,4
            else -> "$timeUnit ${declinationsList[MULTIPLEUNITES]} назад"
        }
    }
}