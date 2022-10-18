package com.stepanusryan.storyapp.api

import com.stepanusryan.storyapp.model.PostResponseRegister
import com.stepanusryan.storyapp.model.ResponseGetStory
import retrofit2.Call
import retrofit2.http.*

interface API {
    @FormUrlEncoded
    @POST("register")
    fun postRegister(
        @Field("name") name:String,
        @Field("email") email:String,
        @Field("password") password:String
    ):Call<PostResponseRegister>
    @POST("login")
    fun postLogin()
    @POST("stories")
    fun postStories()
    @POST("stories/guest")
    fun postStoriesGuest()
    @GET("stories")
    fun getStories(@Header("Authorization") token: String):Call<ResponseGetStory>
}