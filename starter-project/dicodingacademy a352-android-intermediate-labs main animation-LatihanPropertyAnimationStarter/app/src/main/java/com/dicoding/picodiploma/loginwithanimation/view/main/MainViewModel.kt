package com.dicoding.picodiploma.loginwithanimation.view.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.loginwithanimation.data.Result
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import com.dicoding.picodiploma.loginwithanimation.data.response.StoryItem
import kotlinx.coroutines.launch

class MainViewModel(private val repository: UserRepository) : ViewModel() {
    private val _stories = MutableLiveData<Result<List<StoryItem>>>()
    private val _logoutStatus = MutableLiveData<Boolean>()
    val stories: LiveData<Result<List<StoryItem>>> = _stories
    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun getStories() {
        viewModelScope.launch {
            try {
                repository.getStories().collect { result ->
                    _stories.postValue(result)
                }
            } catch (e: Exception) {
                Log.e("VIEW_MODEL_ERROR", e.message ?: "Error in ViewModel")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
            _logoutStatus.postValue(true) // Perbarui LiveData untuk mengindikasikan logout berhasil
        }
    }

}
