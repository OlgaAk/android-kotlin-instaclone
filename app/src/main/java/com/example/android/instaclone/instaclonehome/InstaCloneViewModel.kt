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
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.android.instaclone.database.InstaCloneDatabaseDao
import com.example.android.instaclone.database.ImagePost
import com.example.android.instaclone.formatNights
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
class InstaCloneViewModel(
        val database: InstaCloneDatabaseDao,
        application: Application) : AndroidViewModel(application) {


    private val _response = MutableLiveData<String>()
    val response: LiveData<String>
        get() = _response

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
                } catch (t:Throwable){
                _response.value = "Failure: " + t.message
            }
        }
    }

    //TODO display images from url


    /**
     * Called when the ViewModel is dismantled.
     * At this point, we want to cancel all coroutines;
     * otherwise we end up with processes that have nowhere to return to
     * using memory and resources.
     */

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }


}
