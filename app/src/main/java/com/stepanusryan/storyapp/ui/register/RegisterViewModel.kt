package com.stepanusryan.storyapp.ui.register

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.stepanusryan.storyapp.api.ApiConfig
import com.stepanusryan.storyapp.data.source.AppRepository
import com.stepanusryan.storyapp.model.PostResponseRegister
import com.stepanusryan.storyapp.ui.login.LoginActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel():ViewModel() {
    private val _loading = MutableLiveData<Boolean>()
    val loading : LiveData<Boolean> = _loading

    private val _register = MutableLiveData<String>()
    val register : LiveData<String> = _register

    fun postRegister(context: Context,name:String,email:String,password:String) {
        val client = ApiConfig.getApiService().postRegister(name,email,password)
        client.enqueue(object : Callback<PostResponseRegister> {
            override fun onResponse(call: Call<PostResponseRegister>, response: Response<PostResponseRegister>) {
                val responsePostRegister = response.body()
                if (response.isSuccessful && response.body() != null){
                    val error = responsePostRegister?.error
                    if (!error!!){
                        _loading.value = false
                        _register.value = responsePostRegister.message
                    }
                    else{
                        _loading.value = false
                        _register.value = "Registrasi Gagal"
                    }
                }
                else{
                    _loading.value = false
                    _register.value = "Akun sudah terdaftar"
                }
            }

            override fun onFailure(call: Call<PostResponseRegister>, t: Throwable) {
                _loading.value = false
                _register.value = t.message
            }

        })
    }
}