package com.dicoding.picodiploma.loginwithanimation.view.welcome

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityWelcomeBinding
import com.dicoding.picodiploma.loginwithanimation.view.login.LoginActivity
import com.dicoding.picodiploma.loginwithanimation.view.signup.SignupActivity

class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding
    private val viewModel: WelcomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupObservers()
        setupActions()
        playAnimation()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupObservers() {
        viewModel.navigateToLogin.observe(this, Observer { shouldNavigate ->
            if (shouldNavigate) {
                startActivity(Intent(this, LoginActivity::class.java))
                viewModel.doneNavigating()
            }
        })

        viewModel.navigateToSignup.observe(this, Observer { shouldNavigate ->
            if (shouldNavigate) {
                startActivity(Intent(this, SignupActivity::class.java))
                viewModel.doneNavigating()
            }
        })
    }

    private fun setupActions() {
        binding.loginButton.setOnClickListener {
            viewModel.onLoginClicked()
        }

        binding.signupButton.setOnClickListener {
            viewModel.onSignupClicked()
        }
    }

    private fun playAnimation() {
        binding.titleTextView.visibility = View.VISIBLE
        binding.descTextView.visibility = View.VISIBLE
        binding.loginButton.visibility = View.VISIBLE
        binding.signupButton.visibility = View.VISIBLE

        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
        val login = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(500)
        val signup = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).setDuration(500)
        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(500)
        val desc = ObjectAnimator.ofFloat(binding.descTextView, View.ALPHA, 1f).setDuration(500)
        val together = AnimatorSet().apply {
            play(login).with(signup)
        }
        AnimatorSet().apply {
            playSequentially(title, desc, together)
            start()
        }
    }
}
