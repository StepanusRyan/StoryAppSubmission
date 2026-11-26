package com.stepanusryan.storyapp.api.response

import com.google.gson.annotations.SerializedName
import com.stepanusryan.storyapp.model.Story

data class DetailStoryResponse(
    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("story")
    val story: Story
)
