package com.stepanusryan.storyapp.ui.detail

import android.graphics.Color
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.stepanusryan.storyapp.R
import com.stepanusryan.storyapp.data.ResultState
import com.stepanusryan.storyapp.databinding.ActivityDetailBinding
import com.stepanusryan.storyapp.model.Story
import com.stepanusryan.storyapp.util.applySystemBarsPadding
import com.stepanusryan.storyapp.util.currentLocale
import com.stepanusryan.storyapp.util.toDisplayDate
import com.stepanusryan.storyapp.viewmodel.ViewModelFactory

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val viewModel by viewModels<DetailViewModel> { ViewModelFactory.getInstance(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge(statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT))
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setTitle(R.string.title_detail_story)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.root.applySystemBarsPadding(applyTop = false)

        val storyId = intent.getStringExtra(EXTRA_STORY_ID)
        if (storyId == null) {
            finish()
            return
        }

        binding.btnRetry.setOnClickListener { viewModel.loadStory(storyId) }
        viewModel.story.observe(this) { result -> renderStory(result) }
        if (viewModel.story.value == null) viewModel.loadStory(storyId)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    private fun renderStory(result: ResultState<Story>) {
        binding.progressBar.isVisible = result is ResultState.Loading
        binding.layoutContent.isVisible = result is ResultState.Success
        binding.layoutError.isVisible = result is ResultState.Error
        when (result) {
            is ResultState.Success -> showStory(result.data)
            is ResultState.Error -> {
                binding.tvErrorMessage.text = result.message ?: getString(R.string.error_generic)
            }
            ResultState.Loading -> Unit
        }
    }

    private fun showStory(story: Story) {
        binding.tvDetailName.text = story.name
        binding.tvDetailDate.text = story.createdAt.toDisplayDate(currentLocale)
        binding.tvDetailDescription.text = story.description
        Glide.with(this)
            .load(story.photoUrl)
            .placeholder(R.color.image_placeholder)
            .into(binding.ivDetailPhoto)
    }

    companion object {
        const val EXTRA_STORY_ID = "extra_story_id"
    }
}
