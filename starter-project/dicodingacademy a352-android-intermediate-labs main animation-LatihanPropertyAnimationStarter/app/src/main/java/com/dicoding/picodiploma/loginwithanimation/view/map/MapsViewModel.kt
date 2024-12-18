package com.dicoding.picodiploma.loginwithanimation.view.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import com.dicoding.picodiploma.loginwithanimation.data.response.StoryItem
import kotlinx.coroutines.launch

class MapsViewModel(private val repository: UserRepository) : ViewModel() {
    private val _storiesWithLocation = MutableLiveData<List<StoryItem>>()
    val storiesWithLocation: LiveData<List<StoryItem>> = _storiesWithLocation

    fun getStoriesWithLocation() {
        viewModelScope.launch {
            try {
                val response = repository.getStoriesWithLocation()
                _storiesWithLocation.value = response.listStory
            } catch (e: Exception) {
                // Handle error (you might want to add error handling)
                _storiesWithLocation.value = emptyList()
            }
        }
    }
}