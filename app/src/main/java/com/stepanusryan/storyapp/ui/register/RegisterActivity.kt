package com.stepanusryan.storyapp.ui.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.stepanusryan.storyapp.R
import com.stepanusryan.storyapp.data.ResultState
import com.stepanusryan.storyapp.databinding.ActivityRegisterBinding
import com.stepanusryan.storyapp.util.applySystemBarsPadding
import com.stepanusryan.storyapp.util.hideKeyboard
import com.stepanusryan.storyapp.util.showToast
import com.stepanusryan.storyapp.viewmodel.ViewModelFactory

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val viewModel by viewModels<RegisterViewModel> { ViewModelFactory.getInstance(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.root.applySystemBarsPadding()

        binding.btnRegister.setOnClickListener { register() }
        binding.tvLogin.setOnClickListener { finish() }
        viewModel.registerResult.observe(this) { result -> handleRegisterResult(result) }

        playAnimation()
    }

    private fun register() {
        val name = binding.edRegisterName.text?.toString()?.trim() ?: ""
        binding.tilName.error = if (name.isEmpty()) getString(R.string.error_name_empty) else null
        if (name.isEmpty() || !binding.edRegisterEmail.isValid || !binding.edRegisterPassword.isValid) {
            showToast(getString(R.string.error_form_invalid))
            return
        }
        hideKeyboard()
        viewModel.register(
            name = name,
            email = binding.edRegisterEmail.text?.toString()?.trim() ?: "",
            password = binding.edRegisterPassword.text?.toString() ?: ""
        )
    }

    private fun handleRegisterResult(result: ResultState<Unit>) {
        showLoading(result is ResultState.Loading)
        when (result) {
            is ResultState.Success -> {
                showToast(getString(R.string.register_success))
                finish()
            }
            is ResultState.Error -> showToast(result.message ?: getString(R.string.error_generic))
            ResultState.Loading -> Unit
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.isVisible = isLoading
        binding.btnRegister.isEnabled = !isLoading
    }

    private fun playAnimation() {
        val distance = resources.getDimension(R.dimen.animation_distance)
        val entrance = listOf(
            binding.tvTitle,
            binding.tvSubtitle,
            binding.tilName,
            binding.tilEmail,
            binding.tilPassword,
            binding.btnRegister,
            binding.layoutLogin
        ).map { view ->
            ObjectAnimator.ofPropertyValuesHolder(
                view,
                PropertyValuesHolder.ofFloat(View.ALPHA, 0f, 1f),
                PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, distance, 0f)
            ).setDuration(250)
        }
        AnimatorSet().apply {
            playSequentially(entrance)
            start()
        }
    }
}
