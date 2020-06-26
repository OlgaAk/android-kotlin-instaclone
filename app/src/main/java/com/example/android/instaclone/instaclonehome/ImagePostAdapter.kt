package com.example.android.instaclone.instaclonehome

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

import com.example.android.instaclone.databinding.ListItemImagePostsBinding
import com.example.android.instaclone.network.Post

class ImagePostAdapter(val clickListener: ImagePostListener) :    ListAdapter<Post, ImagePostAdapter.ViewHolder>(DiffCallback()){

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       holder.bind(getItem(position)!!, clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: ListItemImagePostsBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(item: Post, clickListener: ImagePostListener) {
            binding.imagePost = item
            binding.clickListener = clickListener
            Log.d("Myactivity", "Inside viewholder bind. item is " + item.toString())
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ListItemImagePostsBinding.inflate(inflater, parent, false)
                return ViewHolder(binding)
            }
        }

    }

}

class DiffCallback: DiffUtil.ItemCallback<Post>(){
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
    }
}

class ImagePostListener(val clicklistener: (post: Post, v: View) -> Unit){
    fun onClick(post: Post, v: View) = clicklistener(post, v)
}