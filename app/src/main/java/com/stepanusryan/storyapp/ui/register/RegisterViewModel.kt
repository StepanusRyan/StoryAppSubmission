package com.stepanusryan.storyapp.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stepanusryan.storyapp.data.ResultState
import com.stepanusryan.storyapp.data.StoryRepository
import com.stepanusryan.storyapp.data.resultOf
import kotlinx.coroutines.launch

class RegisterViewModel(private val repository: StoryRepository) : ViewModel() {

    private val _registerResult = MutableLiveData<ResultState<Unit>>()
    val registerResult: LiveData<ResultState<Unit>> = _registerResult

    fun register(name: String, email: String, password: String) {
        _registerResult.value = ResultState.Loading
        viewModelScope.launch {
            _registerResult.value = resultOf { repository.register(name, email, password) }
        }
    }
}
