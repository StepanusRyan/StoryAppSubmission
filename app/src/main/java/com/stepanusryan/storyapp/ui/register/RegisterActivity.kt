package com.stepanusryan.storyapp.ui.register

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.stepanusryan.storyapp.api.ApiConfig
import com.stepanusryan.storyapp.databinding.ActivityRegisterBinding
import com.stepanusryan.storyapp.model.PostResponseRegister
import com.stepanusryan.storyapp.ui.login.LoginActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    private lateinit var registerBinding:ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(registerBinding.root)
        registerBinding.btnSignUp.setOnClickListener {
            postRegister(
                registerBinding.edRegisterName.text.toString(),
                registerBinding.edRegisterEmail.text.toString(),
                registerBinding.edRegisterPassword.text.toString()
            )
            val input = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            input.hideSoftInputFromWindow(it.windowToken,0)
        }
    }

    private fun postRegister(name:String,email:String,password:String) {
        registerBinding.progressBar.visibility = View.VISIBLE
        val client = ApiConfig.getApiService().postRegister(name,email,password)
        client.enqueue(object : Callback<PostResponseRegister>{
            override fun onResponse(call: Call<PostResponseRegister>, response: Response<PostResponseRegister>) {
                val responsePostRegister = response.body()
                if (response.isSuccessful && response.body() != null){
                    registerBinding.progressBar.visibility = View.INVISIBLE
                    Log.d("response",responsePostRegister?.message.toString())
                    Toast.makeText(this@RegisterActivity,
                        responsePostRegister?.message.toString(),Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@RegisterActivity,LoginActivity::class.java))
                    finish()
                }
                else{
                    Toast.makeText(this@RegisterActivity,
                        responsePostRegister?.message.toString(),Toast.LENGTH_SHORT).show()

                }
            }

            override fun onFailure(call: Call<PostResponseRegister>, t: Throwable) {
                registerBinding.progressBar.visibility = View.INVISIBLE
                Toast.makeText(this@RegisterActivity, t.message,Toast.LENGTH_SHORT).show()
            }

        })
    }
}