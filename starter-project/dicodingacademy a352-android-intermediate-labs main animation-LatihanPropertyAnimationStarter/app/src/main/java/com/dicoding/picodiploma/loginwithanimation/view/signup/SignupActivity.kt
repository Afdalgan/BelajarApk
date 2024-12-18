package com.dicoding.picodiploma.loginwithanimation.view.signup

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivitySignupBinding
import com.dicoding.picodiploma.loginwithanimation.view.ViewModelFactory

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var viewModel: SignUpViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[SignUpViewModel::class.java]

        setupAction()
    }

    private fun setupAction() {
        binding.signupButton.setOnClickListener {
            val name = binding.edRegisterName.text.toString()
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()

            if (name.isNotBlank() && email.isNotBlank() && password.isNotBlank()) {
                showLoading(true)
                viewModel.register(name, email, password).observe(this) { result ->
                    result.onSuccess { response ->
                        showLoading(false)
                        if (!response.error!!) {
                            Toast.makeText(this, "Register success: ${response.message}", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            Toast.makeText(this, "Failed: ${response.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                    result.onFailure { error ->
                        showLoading(false)
                        Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
