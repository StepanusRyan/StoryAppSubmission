package com.stepanusryan.storyapp.ui.login

import android.content.Context
import android.content.Intent
import androidx.lifecycle.*
import com.stepanusryan.storyapp.api.ApiConfig
import com.stepanusryan.storyapp.model.LoginResult
import com.stepanusryan.storyapp.model.PostLoginResponse
import com.stepanusryan.storyapp.model.User
import com.stepanusryan.storyapp.ui.home.HomeActivity
import com.stepanusryan.storyapp.util.Preference
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val preference: Preference):ViewModel() {
    private val _loading = MutableLiveData<Boolean>()
    val loading : LiveData<Boolean> = _loading

    private val _login = MutableLiveData<String>()
    val login : LiveData<String> = _login

    fun postLogin(context: Context, email:String,password:String) {
        _loading.value = true
        val client = ApiConfig.getApiService().postLogin(email,password)
        client.enqueue(object : Callback<PostLoginResponse>{
            override fun onResponse(call: Call<PostLoginResponse>,response: Response<PostLoginResponse>) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null){
                    val error = responseBody.error
                    if (!error){
                        val login = User(
                            responseBody.loginResult.userId,
                            responseBody.loginResult.name,
                            responseBody.loginResult.token,
                            true
                        )
                        saveKeyUser(login)
                        _loading.value = false
                        _login.value = responseBody.message
                    }
                    else{
                        _loading.value = false
                        _login.value = responseBody.message
                    }
                }
                else{
                    _loading.value = false
                    _login.value = "Email atau Password Salah!"
                }
            }

            override fun onFailure(call: Call<PostLoginResponse>, t: Throwable) {
                _loading.value = false
                _login.value = t.message
            }

        })
    }
    fun getUsers():LiveData<User>{
        return preference.getUser().asLiveData()
    }
    fun saveKeyUser(key: User){
        viewModelScope.launch {
            preference.saveUser(key)
        }
    }
}