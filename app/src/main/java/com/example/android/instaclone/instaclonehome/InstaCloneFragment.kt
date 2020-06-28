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

import android.graphics.Color
import android.media.Image
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.android.instaclone.R
import com.example.android.instaclone.databinding.FragmentInstaCloneBinding
import kotlinx.android.synthetic.main.list_item_image_posts.view.*


class InstaCloneFragment : Fragment() {


    val instaCloneViewModel: InstaCloneViewModel by lazy {
        ViewModelProviders.of(
                this).get(InstaCloneViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding = FragmentInstaCloneBinding.inflate(inflater)

        binding.instaCloneViewModel = instaCloneViewModel

        binding.lifecycleOwner = this

        val adapter = ImagePostAdapter(ImagePostListener { post, view ->
            when (view.id) {
                R.id.icon_like -> {
                    instaCloneViewModel.onLikeClicked(post)
                }
                R.id.icon_comment -> Toast.makeText(context, "Comment icon clicked", Toast.LENGTH_SHORT).show()
                R.id.icon_send -> Toast.makeText(context, "Send icon clicked", Toast.LENGTH_SHORT).show()
                R.id.icon_bookmark -> {
                    val imageView = view as ImageView
                    if (!post.bookMarked) {
                        imageView.setImageResource(R.drawable.ic_bookmark_filled_24px)
                        post.bookMarked = true
                    } else {
                        imageView.setImageResource(R.drawable.ic_bookmark_border_24px)
                        post.bookMarked = false
                    }
                }
                R.id.icon_more -> Toast.makeText(context, "More icon clicked", Toast.LENGTH_SHORT).show()
                else -> {
                }
            }
        })

        binding.postsList.adapter = adapter

        instaCloneViewModel.imagePosts.observe(viewLifecycleOwner, Observer {

            it?.let {
                Log.d("Myapp", "Observer post list changed ${it.get(0).likes}")
                adapter.addHeaderAndSubmitList(it)
                Log.d("Myapp", "list submited")
            }
        })

        instaCloneViewModel.liked.observe(viewLifecycleOwner, Observer {
            changeLikeIcon(it, binding)
        })


        setHasOptionsMenu(true)

        return binding.root
    }

    private fun changeLikeIcon(it: Boolean?, binding: FragmentInstaCloneBinding) {
        val icon = binding.postsList.icon_like
        val text = binding.postsList.text_who_liked
        text.text = resources.getString(R.string.likes, instaCloneViewModel.likes.value)
        if (!it!!) {
            icon.setImageResource(R.drawable.ic_favorite_filled_24px)
        } else {
            icon.setImageResource(R.drawable.ic_favorite_border_24px)
        }
    }


//    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
//        inflater.inflate(R.menu.overflow_menu, menu)
//        super.onCreateOptionsMenu(menu, inflater)
//    } TODO create menu
}
