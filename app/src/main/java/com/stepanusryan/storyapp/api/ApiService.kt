package com.stepanusryan.storyapp.api

import com.stepanusryan.storyapp.api.request.LoginRequest
import com.stepanusryan.storyapp.api.request.RegisterRequest
import com.stepanusryan.storyapp.api.response.DetailStoryResponse
import com.stepanusryan.storyapp.api.response.GeneralResponse
import com.stepanusryan.storyapp.api.response.LoginResponse
import com.stepanusryan.storyapp.api.response.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {
    @POST("register")
    suspend fun register(@Body request: RegisterRequest): GeneralResponse

    @POST("login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @GET("stories")
    suspend fun getStories(@Header("Authorization") token: String): StoryResponse

    @GET("stories/{id}")
    suspend fun getDetailStory(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): DetailStoryResponse

    @Multipart
    @POST("stories")
    suspend fun uploadStory(
        @Header("Authorization") token: String,
        @Part photo: MultipartBody.Part,
        @Part("description") description: RequestBody
    ): GeneralResponse
}
