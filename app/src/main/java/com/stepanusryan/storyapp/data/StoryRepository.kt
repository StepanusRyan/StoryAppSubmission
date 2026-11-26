package com.stepanusryan.storyapp.data

import android.content.Context
import android.net.Uri
import com.stepanusryan.storyapp.api.ApiService
import com.stepanusryan.storyapp.api.request.LoginRequest
import com.stepanusryan.storyapp.api.request.RegisterRequest
import com.stepanusryan.storyapp.data.pref.UserPreference
import com.stepanusryan.storyapp.model.Story
import com.stepanusryan.storyapp.model.User
import com.stepanusryan.storyapp.util.reduceFileImage
import com.stepanusryan.storyapp.util.uriToFile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class StoryRepository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference,
    private val appContext: Context
) {
    fun getSession(): Flow<User> = userPreference.getSession()

    suspend fun logout() = userPreference.logout()

    suspend fun register(name: String, email: String, password: String) {
        apiService.register(RegisterRequest(name, email, password))
    }

    suspend fun login(email: String, password: String): User {
        val result = apiService.login(LoginRequest(email, password)).loginResult
        val user = User(result.userId, result.name, result.token, isLogin = true)
        userPreference.saveSession(user)
        return user
    }

    suspend fun getStories(): List<Story> = apiService.getStories(bearerToken()).listStory

    suspend fun getDetailStory(id: String): Story =
        apiService.getDetailStory(bearerToken(), id).story

    suspend fun uploadStory(imageUri: Uri, description: String) {
        val imageFile = uriToFile(imageUri, appContext).reduceFileImage()
        try {
            val photo = MultipartBody.Part.createFormData(
                "photo",
                imageFile.name,
                imageFile.asRequestBody("image/jpeg".toMediaType())
            )
            val descriptionBody = description.toRequestBody("text/plain".toMediaType())
            apiService.uploadStory(bearerToken(), photo, descriptionBody)
        } finally {
            imageFile.delete()
        }
    }

    private suspend fun bearerToken() = "Bearer ${userPreference.getSession().first().token}"

    companion object {
        @Volatile
        private var INSTANCE: StoryRepository? = null

        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreference,
            context: Context
        ): StoryRepository = INSTANCE ?: synchronized(this) {
            INSTANCE ?: StoryRepository(apiService, userPreference, context.applicationContext)
                .also { INSTANCE = it }
        }
    }
}
