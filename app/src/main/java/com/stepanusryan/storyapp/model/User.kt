package com.stepanusryan.storyapp.model

import com.google.gson.annotations.SerializedName

data class User (
    val userId: String,
    val name: String,
    val token: String,
    val isLogin:Boolean
    )