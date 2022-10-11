package com.stepanusryan.storyapp.data.source.remote

import android.util.Log
import com.stepanusryan.storyapp.api.ApiConfig
import com.stepanusryan.storyapp.model.ResponseGetStory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RemoteDataSource {
    companion object{
        @Volatile
        private var instance:RemoteDataSource?=null
        fun getInstance():RemoteDataSource =
            instance?: synchronized(this){
                instance?: RemoteDataSource().apply { instance = this }
            }
    }
//    fun getStories(callback: LoadStories){
//        val client = ApiConfig.getApiService().getStories(token = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLURJdUlxQkNnM1VIYU8wZ24iLCJpYXQiOjE2NjQwMzI1NTB9.WRTOTqX5CHT-fP4yzE516n26Y4xd918hoBsc-yoPU04")
//        client.enqueue(object : Callback<List<ResponseGetStory>>{
//            override fun onResponse(call: Call<List<ResponseGetStory>>, response: Response<List<ResponseGetStory>>) {
//                callback.storyReceived(response.body()!!)
//            }
//
//            override fun onFailure(call: Call<List<ResponseGetStory>>, t: Throwable) {
//                Log.e("error","${t.message}")
//            }
//
//        })
//    }
//    interface LoadStories{
//        fun storyReceived(story:List<ResponseGetStory>)
//    }
}
