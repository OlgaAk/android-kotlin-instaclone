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

@BindingAdapter("imageUrlRounded")
fun bindImageRounded(imgView: ImageView, imgUrl: String?){
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




