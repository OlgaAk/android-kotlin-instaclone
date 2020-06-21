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


    private val _response = MutableLiveData<String>()
    val response: LiveData<String>
        get() = _response

    private val _imagePosts = MutableLiveData<List<Post>>()
    val imagePosts: LiveData<List<Post>>
        get() = _imagePosts

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    init{
        getImages()
    }

    private fun getImages(){
        uiScope.launch {
            var getImagesDeferred = ImageApi.retrofitService.getImages()
            try{
                var listResult = getImagesDeferred.await()
                    _response.value = listResult.toString()
                    //_response.value = "Size is ${response.body()?.size}"
                if(listResult.size > 0) {
                    _imagePosts.value = listResult
                    Log.d("Myactivity", "Inside Viewmodel getimages. imageposts are " + imagePosts.toString())
                }
                } catch (t:Throwable){
                _response.value = "Failure: " + t.message
            }
        }
    }

    //TODO display images from url



    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }


}
