package com.stepanusryan.storyapp.di

import android.content.Context
import com.stepanusryan.storyapp.data.source.AppRepository
import com.stepanusryan.storyapp.data.source.remote.RemoteDataSource

object Injection {
    fun provideRepository(context: Context):AppRepository{
        val remoteDataSource = RemoteDataSource.getInstance()
        return AppRepository.getInstance(remoteDataSource)
    }
}