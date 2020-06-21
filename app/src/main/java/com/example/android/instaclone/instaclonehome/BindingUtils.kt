/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data : List<Post>?){
    val adapter = recyclerView.adapter as ImagePostAdapter
    adapter.submitList(data)
    Log.d("Myactivity", "Inside bindingutils . data is " + data.toString())
}


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

//@BindingAdapter("sleepDurationFormated")
//fun TextView.setSleepDurationFormated(item: ImagePost?){
//    item?.let{
//        text = convertDurationToFormatted(item.startTimeMilli, item.endTimeMilli, context.resources)
//    }
//}
//
//@BindingAdapter("sleepQualityString")
//fun TextView.convertNumericQualityToString(item: ImagePost?){
//    item?.let{
//        text = com.example.android.instaclone.convertNumericQualityToString(item.sleepQuality, context.resources)
//    }
//}
//
//@BindingAdapter("sleepImage")
//fun ImageView.setSleepImage(item: ImagePost?){
//    item?.let{
//        setImageResource(when (item.sleepQuality) {
//            0 -> R.drawable.ic_sleep_0
//            1 -> R.drawable.ic_sleep_1
//            2 -> R.drawable.ic_sleep_2
//            3 -> R.drawable.ic_sleep_3
//            4 -> R.drawable.ic_sleep_4
//            5 -> R.drawable.ic_sleep_5
//            else -> R.drawable.ic_sleep_active
//        })
//    }
//}