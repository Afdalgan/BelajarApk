package com.dicoding.picodiploma.loginwithanimation.view.welcome

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class WelcomeViewModel : ViewModel() {
    private val _navigateToLogin = MutableLiveData<Boolean>()
    val navigateToLogin: LiveData<Boolean> get() = _navigateToLogin

    private val _navigateToSignup = MutableLiveData<Boolean>()
    val navigateToSignup: LiveData<Boolean> get() = _navigateToSignup

    fun onLoginClicked() {
        _navigateToLogin.value = true
    }

    fun onSignupClicked() {
        _navigateToSignup.value = true
    }

    fun doneNavigating() {
        _navigateToLogin.value = false
        _navigateToSignup.value = false
    }
}
