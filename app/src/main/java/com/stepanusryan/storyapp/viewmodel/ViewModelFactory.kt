package com.stepanusryan.storyapp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.stepanusryan.storyapp.data.source.AppRepository
import com.stepanusryan.storyapp.di.Injection
import com.stepanusryan.storyapp.ui.home.HomeViewModel
import com.stepanusryan.storyapp.ui.login.LoginViewModel
import com.stepanusryan.storyapp.ui.register.RegisterViewModel
import com.stepanusryan.storyapp.util.Preference


class ViewModelFactory(private val preference: Preference,private val context: Context)
    :ViewModelProvider.NewInstanceFactory(){
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when{
            modelClass.isAssignableFrom(HomeViewModel::class.java)->{
                HomeViewModel(preference,Injection.provideRepository(context)) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java)->{
                LoginViewModel(preference) as T
            }
            else -> throw Throwable("Unknown ViewModel class : ${modelClass.name}")
        }
    }
}