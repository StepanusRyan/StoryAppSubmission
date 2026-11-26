package com.stepanusryan.storyapp.ui.home

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.stepanusryan.storyapp.R
import com.stepanusryan.storyapp.data.ResultState
import com.stepanusryan.storyapp.databinding.ActivityHomeBinding
import com.stepanusryan.storyapp.model.Story
import com.stepanusryan.storyapp.ui.addstory.AddStoryActivity
import com.stepanusryan.storyapp.ui.detail.DetailActivity
import com.stepanusryan.storyapp.ui.login.LoginActivity
import com.stepanusryan.storyapp.util.applySystemBarsPadding
import com.stepanusryan.storyapp.util.currentLocale
import com.stepanusryan.storyapp.util.showToast
import com.stepanusryan.storyapp.viewmodel.ViewModelFactory

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val viewModel by viewModels<HomeViewModel> { ViewModelFactory.getInstance(this) }
    private val storyAdapter = StoryAdapter { story -> openDetail(story) }
    private var scrollToTopAfterLoad = false

    private val addStoryLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            scrollToTopAfterLoad = true
            viewModel.loadStories()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge(statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT))
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        binding.root.applySystemBarsPadding(applyTop = false)

        binding.rvStory.layoutManager = LinearLayoutManager(this)
        binding.rvStory.adapter = storyAdapter
        binding.swipeRefresh.setOnRefreshListener { viewModel.loadStories() }
        binding.btnRetry.setOnClickListener { viewModel.loadStories() }
        binding.fabAdd.setOnClickListener {
            addStoryLauncher.launch(Intent(this, AddStoryActivity::class.java))
        }

        viewModel.session.observe(this) { user ->
            if (!user.isLogin) navigateToLogin()
        }
        viewModel.stories.observe(this) { result -> renderStories(result) }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_language -> {
            showLanguageDialog()
            true
        }
        R.id.action_logout -> {
            showLogoutDialog()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun renderStories(result: ResultState<List<Story>>) {
        binding.swipeRefresh.isRefreshing = result is ResultState.Loading
        when (result) {
            ResultState.Loading -> showState(message = null)
            is ResultState.Success -> {
                storyAdapter.submitList(result.data) {
                    if (scrollToTopAfterLoad) {
                        binding.rvStory.scrollToPosition(0)
                        scrollToTopAfterLoad = false
                    }
                }
                showState(message = if (result.data.isEmpty()) getString(R.string.empty_story) else null)
            }
            is ResultState.Error -> {
                val message = result.message ?: getString(R.string.error_generic)
                if (storyAdapter.itemCount == 0) {
                    showState(message, showRetry = true)
                } else {
                    showToast(message)
                }
            }
        }
    }

    private fun showState(message: String?, showRetry: Boolean = false) {
        binding.layoutState.isVisible = message != null
        binding.tvStateMessage.text = message
        binding.btnRetry.isVisible = showRetry
    }

    private fun openDetail(story: Story) {
        val intent = Intent(this, DetailActivity::class.java).apply {
            putExtra(DetailActivity.EXTRA_STORY_ID, story.id)
        }
        startActivity(intent)
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()
    }

    private fun showLanguageDialog() {
        val languageTags = arrayOf("en", "in")
        val languageNames = arrayOf(
            getString(R.string.language_english),
            getString(R.string.language_indonesian)
        )
        val currentLanguage = currentLocale.language
        val checkedItem = if (currentLanguage == "in" || currentLanguage == "id") 1 else 0
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.action_language)
            .setSingleChoiceItems(languageNames, checkedItem) { dialog, which ->
                dialog.dismiss()
                AppCompatDelegate.setApplicationLocales(
                    LocaleListCompat.forLanguageTags(languageTags[which])
                )
            }
            .show()
    }
    private fun showLogoutDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.action_logout)
            .setMessage(R.string.dialog_logout)
            .setPositiveButton(R.string.yes){dialog,_->
                dialog.dismiss()
                viewModel.logout()
            }
            .setNegativeButton(R.string.cancel){dialog,_->
                dialog.dismiss()
            }
            .setCancelable(true)
            .show()
    }
}
