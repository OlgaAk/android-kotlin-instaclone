package com.example.android.instaclone.instaclonehome

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.android.instaclone.R

import com.example.android.instaclone.databinding.ListItemImagePostsBinding
import com.example.android.instaclone.network.Post
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val ITEM_VIEW_TYPE_HEADER = 0
private const val ITEM_VIEW_TYPE_ITEM = 1

class ImagePostAdapter(val clickListener: ImagePostListener) : ListAdapter<DataItem, RecyclerView.ViewHolder>(DiffCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            ITEM_VIEW_TYPE_HEADER -> TextViewHolder.from(parent)
            ITEM_VIEW_TYPE_ITEM -> ViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewtype ${viewType}")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is ViewHolder -> {
                val postItem = getItem(position) as DataItem.PostItem
                holder.bind(postItem.imagePost, clickListener)
            }
        }

    }

    override fun getItemViewType(position: Int): Int {
        return when(getItem(position)){
            is DataItem.Header -> ITEM_VIEW_TYPE_HEADER
            is DataItem.PostItem -> ITEM_VIEW_TYPE_ITEM
        }
    }

    fun addHeaderAndSubmitList(list: List<Post>?){
        adapterScope.launch {
            val items = when(list){
                null -> listOf(DataItem.Header)
                else -> listOf(DataItem.Header) + list.map { DataItem.PostItem(it) }
            }
            withContext(Dispatchers.Main){

                submitList(items)
            }
        }

    }

    class ViewHolder private constructor(val binding: ListItemImagePostsBinding) : RecyclerView.ViewHolder(binding.root) {

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

    class TextViewHolder(view: View) : RecyclerView.ViewHolder(view){
        companion object {
            fun from(parent: ViewGroup) : TextViewHolder{
                val layoutInflater = LayoutInflater.from((parent.context))
                val view = layoutInflater.inflate(R.layout.header, parent, false)
                return TextViewHolder(view)
            }
        }
    }

}

class DiffCallback : DiffUtil.ItemCallback<DataItem>() {
    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem == newItem
    }
}

// clicklistener called from fragment
class ImagePostListener(val clicklistener: (post: Post, v: View) -> Unit) {
    fun onClick(post: Post, v: View) = clicklistener(post, v)
}


// class to implement a header item
sealed class DataItem {
    data class PostItem(val imagePost: Post) : DataItem() {
        override val id = imagePost.id
    }

    object Header : DataItem() {
        override val id = Long.MIN_VALUE.toString()
    }

    abstract val id: String
}