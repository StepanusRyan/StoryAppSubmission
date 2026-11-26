package com.stepanusryan.storyapp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.stepanusryan.storyapp.data.StoryRepository
import com.stepanusryan.storyapp.di.Injection
import com.stepanusryan.storyapp.ui.MainViewModel
import com.stepanusryan.storyapp.ui.addstory.AddStoryViewModel
import com.stepanusryan.storyapp.ui.detail.DetailViewModel
import com.stepanusryan.storyapp.ui.home.HomeViewModel
import com.stepanusryan.storyapp.ui.login.LoginViewModel
import com.stepanusryan.storyapp.ui.register.RegisterViewModel

class ViewModelFactory private constructor(
    private val repository: StoryRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when {
        modelClass.isAssignableFrom(MainViewModel::class.java) -> MainViewModel(repository)
        modelClass.isAssignableFrom(LoginViewModel::class.java) -> LoginViewModel(repository)
        modelClass.isAssignableFrom(RegisterViewModel::class.java) -> RegisterViewModel(repository)
        modelClass.isAssignableFrom(HomeViewModel::class.java) -> HomeViewModel(repository)
        modelClass.isAssignableFrom(DetailViewModel::class.java) -> DetailViewModel(repository)
        modelClass.isAssignableFrom(AddStoryViewModel::class.java) -> AddStoryViewModel(repository)
        else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    } as T

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: ViewModelFactory(Injection.provideRepository(context))
                    .also { INSTANCE = it }
            }
    }
}
