package com.stepanusryan.storyapp.ui.addstory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.stepanusryan.storyapp.databinding.ActivityAddBinding

class AddActivity : AppCompatActivity() {
    private lateinit var addBinding: ActivityAddBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addBinding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(addBinding.root)
    }
}