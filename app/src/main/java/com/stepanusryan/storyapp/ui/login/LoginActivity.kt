package com.stepanusryan.storyapp.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.stepanusryan.storyapp.databinding.ActivityLoginBinding
import com.stepanusryan.storyapp.model.LoginResult
import com.stepanusryan.storyapp.ui.home.HomeActivity
import com.stepanusryan.storyapp.ui.register.RegisterActivity
import com.stepanusryan.storyapp.util.Preference
import com.stepanusryan.storyapp.viewmodel.ViewModelFactory

class LoginActivity : AppCompatActivity() {
    private lateinit var loginBinding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var loginResult: LoginResult
    private val dataStore:DataStore<Preferences> by preferencesDataStore(name = "user_key")
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(loginBinding.root)
        loginViewModel = ViewModelProvider(this, ViewModelFactory(Preference.getInstance(dataStore),
            this)
        )[LoginViewModel::class.java]

        loginBinding.btnSignIn.setOnClickListener {
            when(isLogin()){
                true ->{
                    val email = loginBinding.edLoginEmail.text.toString()
                    val password = loginBinding.edLoginPassword.text.toString()

                    loginViewModel.postLogin(this,email,password)
                    loginViewModel.login
                }
                else->{
                    Toast.makeText(this@LoginActivity,"Email atau Password salah",Toast.LENGTH_SHORT).show()
                }
            }
        }
        loginViewModel.login.observe(this){
            startActivity(Intent(this@LoginActivity,HomeActivity::class.java))
            finish()
            Toast.makeText(this@LoginActivity,it,Toast.LENGTH_SHORT).show()
        }
        loginViewModel.loading.observe(this){
            showLoading(it)
        }
        loginViewModel.getUsers().observe(this){
            this.loginResult = it
            if (this.loginResult.isLogin){
                val intent = Intent(this@LoginActivity,HomeActivity::class.java)
                intent.putExtra(HomeActivity.EXTRA_TOKEN,loginResult.token)
                startActivity(intent)
                finish()
            }
        }

        loginBinding.txtSignUp.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }
    }
    private fun isLogin() =
                loginBinding.edLoginEmail.error == null &&
                loginBinding.edLoginPassword.error == null &&
                !loginBinding.edLoginEmail.text.isNullOrEmpty() &&
                !loginBinding.edLoginPassword.text.isNullOrEmpty()

    private fun showLoading(status:Boolean){
        if (status){
            loginBinding.progressBar.visibility = View.VISIBLE
        }
        else{
            loginBinding.progressBar.visibility = View.INVISIBLE
        }
    }
}