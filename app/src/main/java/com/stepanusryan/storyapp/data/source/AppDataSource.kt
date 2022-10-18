package com.stepanusryan.storyapp.data.source

import androidx.lifecycle.LiveData
import com.stepanusryan.storyapp.model.ListStoryItem
import com.stepanusryan.storyapp.model.ResponseGetStory

interface AppDataSource {
    fun getStories():LiveData<List<ListStoryItem>>
}