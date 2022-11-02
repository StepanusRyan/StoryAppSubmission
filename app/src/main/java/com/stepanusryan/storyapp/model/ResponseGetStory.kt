package com.stepanusryan.storyapp.model

import com.google.gson.annotations.SerializedName

data class ResponseGetStory(

	@field:SerializedName("listStory")
	val listStory: List<ListStoryItem>,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)

