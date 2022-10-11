package com.stepanusryan.storyapp.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.stepanusryan.storyapp.api.ApiConfig
import com.stepanusryan.storyapp.model.ListStoryItem
import com.stepanusryan.storyapp.model.ResponseGetStory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {
    private var listStory = MutableLiveData<List<ListStoryItem>>()
    fun getStory(){
        val client = ApiConfig.getApiService().getStories(token = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLURJdUlxQkNnM1VIYU8wZ24iLCJpYXQiOjE2NjQwMzI1NTB9.WRTOTqX5CHT-fP4yzE516n26Y4xd918hoBsc-yoPU04")
        client.enqueue(object : Callback<ResponseGetStory> {
            override fun onResponse(call: Call<ResponseGetStory>,response: Response<ResponseGetStory>) {
                if (response.body() != null){
                    listStory.value = response.body()!!.listStory
                }
            }

            override fun onFailure(call: Call<ResponseGetStory>, t: Throwable) {
                Log.e("error","${t.message}")
            }

        })
    }
    fun observeStoryLiveData():LiveData<List<ListStoryItem>>{
        return listStory
    }
}