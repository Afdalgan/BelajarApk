package com.dicoding.picodiploma.loginwithanimation.view.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import com.dicoding.picodiploma.loginwithanimation.data.response.RegisterResponse
import kotlinx.coroutines.launch

class SignUpViewModel(private val userRepository: UserRepository) : ViewModel() {

    fun register(name: String, email: String, password: String): LiveData<Result<RegisterResponse>> {
        val result = MutableLiveData<Result<RegisterResponse>>()
        viewModelScope.launch {
            try {
                val response = userRepository.registerUser(name, email, password)
                result.postValue(Result.success(response))
            } catch (e: Exception) {
                result.postValue(Result.failure(e))
            }
        }
        return result
    }
}
