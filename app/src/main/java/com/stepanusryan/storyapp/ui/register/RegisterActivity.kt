package com.stepanusryan.storyapp.ui.register

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.stepanusryan.storyapp.api.ApiConfig
import com.stepanusryan.storyapp.databinding.ActivityRegisterBinding
import com.stepanusryan.storyapp.model.PostResponseRegister
import com.stepanusryan.storyapp.ui.login.LoginActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    private lateinit var registerBinding:ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(registerBinding.root)
        registerViewModel = ViewModelProvider(this)[RegisterViewModel::class.java]

        registerViewModel.loading.observe(this){
            showLoading(it)
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }

        registerBinding.btnSignUp.setOnClickListener {
            val name = registerBinding.edRegisterName.text.toString()
            val email =  registerBinding.edRegisterEmail.text.toString()
            val password = registerBinding.edRegisterPassword.text.toString()
            val input = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            input.hideSoftInputFromWindow(it.windowToken,0)

            if (registerBinding.edRegisterEmail.error == null && registerBinding.edRegisterPassword.error == null){
                registerViewModel.postRegister(this,name,email,password)
            }
            finish()
        }
        registerViewModel.register.observe(this){
            Toast.makeText(this@RegisterActivity,it.toString(),Toast.LENGTH_SHORT).show()
        }
    }

    private fun showLoading(status:Boolean){
        if (status){
            registerBinding.progressBar.visibility = View.VISIBLE
        }
        else{
            registerBinding.progressBar.visibility = View.INVISIBLE
        }
    }
}