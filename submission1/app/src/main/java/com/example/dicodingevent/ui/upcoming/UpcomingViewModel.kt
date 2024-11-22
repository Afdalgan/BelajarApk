package com.example.dicodingevent.ui.upcoming

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dicodingevent.data.EventRepository
import com.example.dicodingevent.data.entity.EventEntity
import com.example.dicodingevent.data.Result
import kotlinx.coroutines.launch

class UpcomingViewModel(private val eventRepository: EventRepository) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
//    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _eventData: MutableLiveData<Result<List<EventEntity>>> = MutableLiveData()
    val eventData: LiveData<Result<List<EventEntity>>> get() = _eventData

    fun getHeadlineEvent() {
        Log.d("UpcomingViewModel", "Memulai getHeadlineEvent")
        viewModelScope.launch {
            eventRepository.getHeadlineEvent().observeForever { result ->
                when (result) {
                    is Result.Loading -> _isLoading.value = true
                    is Result.Success -> _isLoading.value = false
                    is Result.Error -> {
                        _isLoading.value = false
                        Log.e("UpcomingViewModel", "Error mengambil headline event: ${result.error}")
                    }
                }
            }
        }
    }

    fun getEventsByFinished(finished: Int) {
        _eventData.value = Result.Loading
        viewModelScope.launch {
            eventRepository.getEventsByFinished(finished).observeForever { result ->
                _eventData.value = result
            }
        }
    }
}