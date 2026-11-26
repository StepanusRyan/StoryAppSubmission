package com.stepanusryan.storyapp.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stepanusryan.storyapp.data.ResultState
import com.stepanusryan.storyapp.data.StoryRepository
import com.stepanusryan.storyapp.data.resultOf
import com.stepanusryan.storyapp.model.User
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: StoryRepository) : ViewModel() {

    private val _loginResult = MutableLiveData<ResultState<User>>()
    val loginResult: LiveData<ResultState<User>> = _loginResult

    fun login(email: String, password: String) {
        _loginResult.value = ResultState.Loading
        viewModelScope.launch {
            _loginResult.value = resultOf { repository.login(email, password) }
        }
    }
}
