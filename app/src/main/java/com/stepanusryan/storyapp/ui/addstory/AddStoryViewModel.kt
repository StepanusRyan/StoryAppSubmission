package com.stepanusryan.storyapp.ui.addstory

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stepanusryan.storyapp.data.ResultState
import com.stepanusryan.storyapp.data.StoryRepository
import com.stepanusryan.storyapp.data.resultOf
import kotlinx.coroutines.launch

class AddStoryViewModel(private val repository: StoryRepository) : ViewModel() {
    var currentImageUri: Uri? = null
    var pendingCameraUri: Uri? = null

    private val _uploadResult = MutableLiveData<ResultState<Unit>>()
    val uploadResult: LiveData<ResultState<Unit>> = _uploadResult

    fun uploadStory(imageUri: Uri, description: String) {
        _uploadResult.value = ResultState.Loading
        viewModelScope.launch {
            _uploadResult.value = resultOf { repository.uploadStory(imageUri, description) }
        }
    }
}
