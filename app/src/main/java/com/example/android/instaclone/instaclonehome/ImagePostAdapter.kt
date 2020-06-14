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

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.android.instaclone.database.ImagePost
import com.example.android.instaclone.databinding.ListItemImagePostsBinding

class ImagePostAdapter (val clickListener: SleepNightListener):    ListAdapter<ImagePost, ImagePostAdapter.ViewHolder>(SleepNightDiffCallback()){
   /* var data = listOf<SleepNight>()
        set(value) {
            field = value
            notifyDataSetChanged()
        } /// was used with RecyclerView.Adapter<SleepNightAdapter.ViewHolder>
*/

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!, clickListener)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: ListItemImagePostsBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(item: ImagePost, clickListener: SleepNightListener) {
            binding.sleep = item
            binding.clickListener = clickListener
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

class SleepNightDiffCallback: DiffUtil.ItemCallback<ImagePost>(){
    override fun areItemsTheSame(oldItem: ImagePost, newItem: ImagePost): Boolean {
        return oldItem.nightId == newItem.nightId
    }

    override fun areContentsTheSame(oldItem: ImagePost, newItem: ImagePost): Boolean {
        return oldItem == newItem
    }
}

class SleepNightListener(val clickListener: (sleepId:Long)->Unit){
    fun onClick(night:ImagePost) = clickListener(night.nightId)
}