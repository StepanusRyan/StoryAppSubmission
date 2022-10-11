package com.stepanusryan.storyapp.api

import com.stepanusryan.storyapp.model.ResponseGetStory
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface API {
    @POST("register")
    fun postRegister()
    @POST("login")
    fun postLogin()
    @POST("stories")
    fun postStories()
    @POST("stories/guest")
    fun postStoriesGuest()
    @GET("stories")
    fun getStories(@Header("Authorization") token: String):Call<ResponseGetStory>
}