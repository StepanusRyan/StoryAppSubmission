package com.stepanusryan.storyapp.ui.home

import androidx.lifecycle.ViewModel
import com.stepanusryan.storyapp.data.source.AppRepository
import com.stepanusryan.storyapp.util.Preference

class HomeViewModel(private val preference: Preference,private val appRepository: AppRepository) : ViewModel() {
    fun getStory() = appRepository.getStories()
}