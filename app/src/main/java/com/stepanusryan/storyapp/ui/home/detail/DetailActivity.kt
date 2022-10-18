package com.stepanusryan.storyapp.ui.home.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.stepanusryan.storyapp.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val detailBinding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(detailBinding.root)

    }
}