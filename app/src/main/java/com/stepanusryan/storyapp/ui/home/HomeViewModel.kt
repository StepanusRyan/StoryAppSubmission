package com.stepanusryan.storyapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.stepanusryan.storyapp.data.ResultState
import com.stepanusryan.storyapp.data.StoryRepository
import com.stepanusryan.storyapp.data.resultOf
import com.stepanusryan.storyapp.model.Story
import com.stepanusryan.storyapp.model.User
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: StoryRepository) : ViewModel() {

    val session: LiveData<User> = repository.getSession().asLiveData()

    private val _stories = MutableLiveData<ResultState<List<Story>>>()
    val stories: LiveData<ResultState<List<Story>>> = _stories

    init {
        loadStories()
    }

    fun loadStories() {
        _stories.value = ResultState.Loading
        viewModelScope.launch {
            _stories.value = resultOf { repository.getStories() }
        }
    }

    fun logout() {
        viewModelScope.launch { repository.logout() }
    }
}
