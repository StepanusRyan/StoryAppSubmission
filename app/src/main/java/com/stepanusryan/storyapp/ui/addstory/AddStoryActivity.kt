package com.stepanusryan.storyapp.ui.addstory

import android.content.ActivityNotFoundException
import android.graphics.Color
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.stepanusryan.storyapp.R
import com.stepanusryan.storyapp.data.ResultState
import com.stepanusryan.storyapp.databinding.ActivityAddStoryBinding
import com.stepanusryan.storyapp.util.applySystemBarsPadding
import com.stepanusryan.storyapp.util.getImageUri
import com.stepanusryan.storyapp.util.hideKeyboard
import com.stepanusryan.storyapp.util.showToast
import com.stepanusryan.storyapp.viewmodel.ViewModelFactory

class AddStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddStoryBinding
    private val viewModel by viewModels<AddStoryViewModel> { ViewModelFactory.getInstance(this) }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            viewModel.currentImageUri = uri
            showImage()
        }
    }

    private val launcherCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            viewModel.currentImageUri = viewModel.pendingCameraUri
            showImage()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge(statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT))
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setTitle(R.string.title_add_story)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.root.applySystemBarsPadding(applyTop = false)

        showImage()
        binding.btnGallery.setOnClickListener {
            launcherGallery.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }
        binding.btnCamera.setOnClickListener { startCamera() }
        binding.buttonAdd.setOnClickListener { uploadStory() }
        viewModel.uploadResult.observe(this) { result -> handleUploadResult(result) }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    private fun startCamera() {
        val uri = getImageUri(this)
        viewModel.pendingCameraUri = uri
        try {
            launcherCamera.launch(uri)
        } catch (e: ActivityNotFoundException) {
            showToast(getString(R.string.error_no_camera))
        }
    }

    private fun showImage() {
        val uri = viewModel.currentImageUri ?: return
        Glide.with(this).load(uri).into(binding.ivPreview)
    }

    private fun uploadStory() {
        val imageUri = viewModel.currentImageUri
        if (imageUri == null) {
            showToast(getString(R.string.error_image_empty))
            return
        }
        val description = binding.edAddDescription.text.toString().trim()
        if (description.isEmpty()) {
            binding.tilDescription.error = getString(R.string.error_description_empty)
            return
        }
        binding.tilDescription.error = null
        hideKeyboard()
        viewModel.uploadStory(imageUri, description)
    }

    private fun handleUploadResult(result: ResultState<Unit>) {
        showLoading(result is ResultState.Loading)
        when (result) {
            is ResultState.Success -> {
                showToast(getString(R.string.upload_success))
                setResult(RESULT_OK)
                finish()
            }
            is ResultState.Error -> showToast(result.message ?: getString(R.string.error_generic))
            ResultState.Loading -> Unit
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.isVisible = isLoading
        binding.buttonAdd.isEnabled = !isLoading
        binding.btnGallery.isEnabled = !isLoading
        binding.btnCamera.isEnabled = !isLoading
    }
}
