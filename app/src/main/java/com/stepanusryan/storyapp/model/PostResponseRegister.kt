package com.stepanusryan.storyapp.model

import com.google.gson.annotations.SerializedName

data class PostResponseRegister(
    @field:SerializedName("error")
    val error : String,
    @field:SerializedName("message")
    val message : String
)
data class Register(
    @field:SerializedName("name")
    val name : String,
    @field:SerializedName("email")
    val email : String,
    @field:SerializedName("password")
    val password : String
)
