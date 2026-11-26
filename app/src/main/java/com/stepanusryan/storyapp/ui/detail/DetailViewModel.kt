package com.stepanusryan.storyapp.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stepanusryan.storyapp.data.ResultState
import com.stepanusryan.storyapp.data.StoryRepository
import com.stepanusryan.storyapp.data.resultOf
import com.stepanusryan.storyapp.model.Story
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: StoryRepository) : ViewModel() {

    private val _story = MutableLiveData<ResultState<Story>>()
    val story: LiveData<ResultState<Story>> = _story

    fun loadStory(id: String) {
        _story.value = ResultState.Loading
        viewModelScope.launch {
            _story.value = resultOf { repository.getDetailStory(id) }
        }
    }
}
