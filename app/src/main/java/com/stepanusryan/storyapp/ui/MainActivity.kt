package com.stepanusryan.storyapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.stepanusryan.storyapp.ui.home.HomeActivity
import com.stepanusryan.storyapp.ui.login.LoginActivity
import com.stepanusryan.storyapp.viewmodel.ViewModelFactory

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel> { ViewModelFactory.getInstance(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        splashScreen.setKeepOnScreenCondition { true }

        viewModel.session.observe(this) { user ->
            if (isFinishing) return@observe
            val destination = if (user.isLogin) HomeActivity::class.java else LoginActivity::class.java
            startActivity(Intent(this, destination))
            finish()
        }
    }
}
