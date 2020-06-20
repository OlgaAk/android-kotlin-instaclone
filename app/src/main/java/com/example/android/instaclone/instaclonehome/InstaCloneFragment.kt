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

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.android.instaclone.R
import com.example.android.instaclone.database.InstaCloneDatabase
import com.example.android.instaclone.databinding.FragmentInstaCloneBinding

/**
 * A fragment with buttons to record start and end times for sleep, which are saved in
 * a database. Cumulative data is displayed in a simple scrollable TextView.
 * (Because we have not learned about RecyclerView yet.)
 */
class InstaCloneFragment : Fragment() {

    /**
     * Called when the Fragment is ready to display content to the screen.
     *
     * This function uses DataBindingUtil to inflate R.layout.fragment_sleep_quality.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Get a reference to the binding object and inflate the fragment views.
        val binding: FragmentInstaCloneBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_insta_clone, container, false)

        val application = requireNotNull(this.activity).application

        val dataSource = InstaCloneDatabase.getInstance(application).instaCloneDatabaseDao

        val viewModelFactory = InstaCloneViewModelFactory(dataSource, application)

        val instaCloneViewModel =
                ViewModelProviders.of(
                        this, viewModelFactory).get(InstaCloneViewModel::class.java)

        binding.instaCloneViewModel = instaCloneViewModel

        val adapter = ImagePostAdapter(SleepNightListener {

    })
        binding.postsList.adapter = adapter




        binding.setLifecycleOwner(this)



        return binding.root
    }
}
