package com.stepanusryan.storyapp.ui.login.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.stepanusryan.storyapp.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val registerBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(registerBinding.root)
    }
}