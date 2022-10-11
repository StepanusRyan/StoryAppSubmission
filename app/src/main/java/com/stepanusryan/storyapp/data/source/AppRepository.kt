package com.stepanusryan.storyapp.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.stepanusryan.storyapp.data.source.remote.RemoteDataSource
import com.stepanusryan.storyapp.model.ResponseGetStory
import java.util.ArrayList

class AppRepository private constructor(private val remoteDataSource: RemoteDataSource):AppDataSource{
    companion object{
        @Volatile
        private var instance:AppRepository ?= null
        fun getInstance(remoteDataSource: RemoteDataSource):AppRepository =
            instance ?: synchronized(this){
                instance ?: AppRepository(remoteDataSource).apply { instance = this }
            }
    }
//    override fun getStories(): LiveData<List<ResponseGetStory>> {
//        val dataStory = MutableLiveData<List<ResponseGetStory>>()
//        remoteDataSource.getStories(object : RemoteDataSource.LoadStories{
//            override fun storyReceived(story: List<ResponseGetStory>) {
//                val storyList = ArrayList<ResponseGetStory>()
//                for (response in story){
//                    val stories = ResponseGetStory(
//                        response.listStory,
//                        response.error,
//                        response.message
//                    )
////                    storyList.addAll(stories.l)
//                }
//            }
//
//        })
//        return  dataStory
//    }
}