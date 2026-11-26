package com.stepanusryan.storyapp.api.response

import com.google.gson.annotations.SerializedName
import com.stepanusryan.storyapp.model.Story

data class StoryResponse(
    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("listStory")
    val listStory: List<Story>
)
