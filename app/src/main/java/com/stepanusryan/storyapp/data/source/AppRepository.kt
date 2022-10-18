package com.stepanusryan.storyapp.data.source

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.stepanusryan.storyapp.data.source.remote.RemoteDataSource
import com.stepanusryan.storyapp.model.ListStoryItem
import com.stepanusryan.storyapp.model.ResponseGetStory
import kotlin.collections.ArrayList

class AppRepository private constructor(private val remoteDataSource: RemoteDataSource):AppDataSource{
    companion object{
        @Volatile
        private var instance:AppRepository ?= null
        fun getInstance(remoteDataSource: RemoteDataSource):AppRepository =
            instance ?: synchronized(this){
                instance ?: AppRepository(remoteDataSource).apply { instance = this }
            }
    }

    override fun getStories(): LiveData<List<ListStoryItem>> {
        val dataStory = MutableLiveData<List<ListStoryItem>>()
        remoteDataSource.getStories(object : RemoteDataSource.LoadStories{
            override fun storyReceived(story: ResponseGetStory) {
                val storyList = ArrayList<ListStoryItem>()
                Log.d("response","$story")
                for (response in story.listStory as ArrayList<ListStoryItem>){
                    val storys = ListStoryItem(
                        response.id,
                        response.name,
                        response.description,
                        response.photoUrl,
                        response.createdAt,
                        response.lat,
                        response.lon
                    )
                    storyList.add(storys)
                }
                dataStory.postValue(storyList)
            }
        })
        return dataStory
    }
}