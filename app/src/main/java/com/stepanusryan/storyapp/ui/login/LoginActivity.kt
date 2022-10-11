package com.stepanusryan.storyapp.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.stepanusryan.storyapp.databinding.ActivityLoginBinding
import com.stepanusryan.storyapp.ui.home.HomeActivity
import com.stepanusryan.storyapp.ui.login.register.RegisterActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        val loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(loginBinding.root)

        loginBinding.txtSignUp.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }

        loginBinding.btnSignIn.setOnClickListener {
            startActivity(Intent(this@LoginActivity,HomeActivity::class.java))
            finish()
        }
    }
}