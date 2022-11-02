package com.stepanusryan.storyapp.api

import com.stepanusryan.storyapp.model.PostLoginResponse
import com.stepanusryan.storyapp.model.PostResponseRegister
import com.stepanusryan.storyapp.model.PostStoriesResponse
import com.stepanusryan.storyapp.model.ResponseGetStory
import okhttp3.MultipartBody
import okhttp3.RequestBody
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

    @FormUrlEncoded
    @POST("login")
    fun postLogin(
        @Field("email") email:String,
        @Field("password") password:String
    ):Call<PostLoginResponse>

    @Multipart
    @POST("stories")
    fun postStories(
        @Header("Authorization") token: String,
        @Part files: MultipartBody.Part,
        @Part("description") desc : RequestBody,
        @Part("lat") lat : RequestBody,
        @Part("lon") lon : RequestBody
    ):Call<PostStoriesResponse>

    @POST("stories/guest")
    fun postStoriesGuest()

    @GET("stories")
    fun getStories(
        @Header("Authorization") token: String)
    :Call<ResponseGetStory>

    @GET("stories/:id")
    fun getDetailStories(@Header("Authorization") token: String):Call<ResponseGetStory>
}