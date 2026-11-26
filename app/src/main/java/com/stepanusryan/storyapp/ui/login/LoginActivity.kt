package com.stepanusryan.storyapp.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.stepanusryan.storyapp.R
import com.stepanusryan.storyapp.data.ResultState
import com.stepanusryan.storyapp.databinding.ActivityLoginBinding
import com.stepanusryan.storyapp.model.User
import com.stepanusryan.storyapp.ui.home.HomeActivity
import com.stepanusryan.storyapp.ui.register.RegisterActivity
import com.stepanusryan.storyapp.util.applySystemBarsPadding
import com.stepanusryan.storyapp.util.hideKeyboard
import com.stepanusryan.storyapp.util.showToast
import com.stepanusryan.storyapp.viewmodel.ViewModelFactory

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel by viewModels<LoginViewModel> { ViewModelFactory.getInstance(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.root.applySystemBarsPadding()

        binding.btnLogin.setOnClickListener { login() }
        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
        viewModel.loginResult.observe(this) { result -> handleLoginResult(result) }

        playAnimation()
    }

    private fun login() {
        if (!binding.edLoginEmail.isValid || !binding.edLoginPassword.isValid) {
            showToast(getString(R.string.error_form_invalid))
            return
        }
        hideKeyboard()
        viewModel.login(
            email = binding.edLoginEmail.text?.toString()?.trim() ?: "",
            password = binding.edLoginPassword.text?.toString() ?: ""
        )
    }

    private fun handleLoginResult(result: ResultState<User>) {
        showLoading(result is ResultState.Loading)
        when (result) {
            is ResultState.Success -> {
                showToast(getString(R.string.login_success, result.data.name))
                val intent = Intent(this, HomeActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(intent)
                finish()
            }
            is ResultState.Error -> showToast(result.message ?: getString(R.string.error_generic))
            ResultState.Loading -> Unit
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.isVisible = isLoading
        binding.btnLogin.isEnabled = !isLoading
    }

    private fun playAnimation() {
        val distance = resources.getDimension(R.dimen.animation_distance)
        val logo = ObjectAnimator.ofPropertyValuesHolder(
            binding.ivLogo,
            PropertyValuesHolder.ofFloat(View.ALPHA, 0f, 1f),
            PropertyValuesHolder.ofFloat(View.SCALE_X, 0.5f, 1f),
            PropertyValuesHolder.ofFloat(View.SCALE_Y, 0.5f, 1f)
        ).apply {
            duration = 500
            interpolator = OvershootInterpolator()
        }

        val entrance = listOf(
            binding.tvTitle,
            binding.tvSubtitle,
            binding.tilEmail,
            binding.tilPassword,
            binding.btnLogin,
            binding.layoutRegister
        ).map { view ->
            ObjectAnimator.ofPropertyValuesHolder(
                view,
                PropertyValuesHolder.ofFloat(View.ALPHA, 0f, 1f),
                PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, distance, 0f)
            ).setDuration(250)
        }
        AnimatorSet().apply {
            playSequentially(listOf(logo) + entrance)
            start()
        }
    }
}
