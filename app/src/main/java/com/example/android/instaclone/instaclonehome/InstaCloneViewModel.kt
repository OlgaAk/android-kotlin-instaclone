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

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.android.instaclone.R

import com.example.android.instaclone.network.ImageApi
import com.example.android.instaclone.network.Post
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Response

/**
 * ViewModel for SleepTrackerFragment.
 */
class InstaCloneViewModel : ViewModel() {


    private var _response = MutableLiveData<String>()
    val response: LiveData<String>
        get() = _response

    private var _imagePosts = MutableLiveData<List<Post>>()
    val imagePosts: LiveData<List<Post>>
        get() = _imagePosts

    private val _liked = MutableLiveData<Boolean>()
    val liked: LiveData<Boolean>
        get() = _liked

    private val _likes = MutableLiveData<Long>()
    val likes: LiveData<Long>
        get() = _likes


    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    init {
        getImages()
    }

    private fun getImages() {
        uiScope.launch {
            var getImagesDeferred = ImageApi.retrofitService.getImages()
            try {
                var listResult = getImagesDeferred.await()
                _response.value = listResult.toString()
                //_response.value = "Size is ${response.body()?.size}"
                if (listResult.size > 0) {
                    _imagePosts.value = listResult
                    Log.d("Myactivity", "Inside Viewmodel getimages. imageposts are " + imagePosts.toString())
                }
            } catch (t: Throwable) {
                _response.value = "Failure: " + t.message
            }
        }
    }


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun onLikeClicked(post: Post) {
       if (post!= null) {
           _likes.value = post.likes
                if (post.liked_by_user) {
                    post.liked_by_user = false
                    post.likes --
                    _liked.value = false
                } else {
                    post.liked_by_user = true
                    post.likes ++
                    _liked.value = true
                }
           _likes.value = post.likes
           _imagePosts.value = _imagePosts.value
           Log.d("Myapp", "mutable ${_imagePosts.value?.get(0)?.liked_by_user} ${_imagePosts.value?.get(0)?.likes}")

       }

    }


}
