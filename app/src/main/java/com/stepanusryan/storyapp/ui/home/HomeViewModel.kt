package com.stepanusryan.storyapp.ui.home

import androidx.lifecycle.ViewModel
import com.stepanusryan.storyapp.data.source.AppRepository

class HomeViewModel(private val appRepository: AppRepository) : ViewModel() {
    fun getStory() = appRepository.getStories()
}