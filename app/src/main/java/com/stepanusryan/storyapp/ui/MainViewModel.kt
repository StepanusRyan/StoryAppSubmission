package com.stepanusryan.storyapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.stepanusryan.storyapp.data.StoryRepository
import com.stepanusryan.storyapp.model.User

class MainViewModel(repository: StoryRepository) : ViewModel() {
    val session: LiveData<User> = repository.getSession().asLiveData()
}
