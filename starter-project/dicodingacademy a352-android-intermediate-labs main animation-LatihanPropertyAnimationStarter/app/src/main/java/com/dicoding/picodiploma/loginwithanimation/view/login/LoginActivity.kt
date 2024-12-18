package com.dicoding.picodiploma.loginwithanimation.view.login

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityLoginBinding
import com.dicoding.picodiploma.loginwithanimation.view.ViewModelFactory
import com.dicoding.picodiploma.loginwithanimation.view.main.MainActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()
        setupAction()
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

    private fun setupViewModel() {
        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]
    }

    private fun setupAction() {
        binding.loginButton.setOnClickListener {
            val email = binding.edLoginEmail.text.toString().trim()
            val password = binding.edLoginPassword.text.toString().trim()

            if (email.isEmpty()) {
                binding.edLoginEmail.error = "Email must not be empty"
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                binding.edLoginPassword.error = "Password must not be empty"
                return@setOnClickListener
            }

            binding.progressBar.visibility = android.view.View.VISIBLE

            viewModel.login(email, password).observe(this) { result ->
                binding.progressBar.visibility = android.view.View.GONE

                result.onSuccess { response ->
                    if (response.error == false) {
                        val loginResult = response.loginResult
                        if (loginResult != null) {
                            val user = UserModel(
                                email = email,
                                token = loginResult.token ?: "",
                                isLogin = true
                            )
                            viewModel.saveSession(user)

                            Toast.makeText(this, "Login success!", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, MainActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                            finish()
                        }
                    } else {
                        Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
                    }
                }
                result.onFailure { e ->
                    Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
