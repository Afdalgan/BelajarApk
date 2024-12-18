package com.dicoding.picodiploma.loginwithanimation.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import com.dicoding.picodiploma.loginwithanimation.data.response.StoryItem
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

class MainViewModel(
    private val repository: UserRepository
) : ViewModel() {

    private val _logoutStatus = MutableLiveData<Boolean>()
    val logoutStatus: LiveData<Boolean> get() = _logoutStatus

    private val _storiesPaging = MutableLiveData<PagingData<StoryItem>>()
    val storiesPaging: LiveData<PagingData<StoryItem>> get() = _storiesPaging

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun fetchStoriesPaging() {
        fun fetchStoriesPaging() {
            viewModelScope.launch {
                repository.getStoriesPaging()
                    .cachedIn(viewModelScope)
                    .collectLatest { pagingData ->
                        _storiesPaging.postValue(pagingData)
                    }
            }
        }

        viewModelScope.launch {
            try {
                repository.getStoriesPaging()
                        .cachedIn(viewModelScope)
                        .collectLatest { pagingData ->
                            _storiesPaging.postValue(pagingData)
                            println("Data berhasil dimuat: ${pagingData}")
                        }

            } catch (e: Exception) {
                println("Error saat memuat data: ${e.message}")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
            _logoutStatus.postValue(true)
        }
    }
}
