package com.stepanusryan.storyapp.di

import android.content.Context
import com.stepanusryan.storyapp.api.ApiConfig
import com.stepanusryan.storyapp.data.StoryRepository
import com.stepanusryan.storyapp.data.pref.UserPreference
import com.stepanusryan.storyapp.data.pref.dataStore

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val appContext = context.applicationContext
        val userPreference = UserPreference.getInstance(appContext.dataStore)
        return StoryRepository.getInstance(ApiConfig.apiService, userPreference, appContext)
    }
}
