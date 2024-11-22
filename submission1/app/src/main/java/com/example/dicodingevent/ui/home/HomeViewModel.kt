package com.example.dicodingevent.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dicodingevent.data.EventRepository
import com.example.dicodingevent.data.entity.EventEntity
import com.example.dicodingevent.data.Result
import kotlinx.coroutines.launch

class HomeViewModel(private val eventRepository: EventRepository) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
//    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _eventData: MutableLiveData<Result<List<EventEntity>>> = MutableLiveData()
    val eventData: LiveData<Result<List<EventEntity>>> get() = _eventData

    private val _upcomingEventData: MutableLiveData<Result<List<EventEntity>>> = MutableLiveData()
    val upcomingEventData: LiveData<Result<List<EventEntity>>> get() = _upcomingEventData

    fun getHeadlineEvent() {
        Log.d("FinishedViewModel", "Memulai getHeadlineEvent")
        viewModelScope.launch {
            eventRepository.getHeadlineEvent().observeForever { result ->
                when (result) {
                    is Result.Loading -> _isLoading.value = true
                    is Result.Success -> _isLoading.value = false
                    is Result.Error -> {
                        _isLoading.value = false
                        Log.e("FinishedViewModel", "Error mengambil headline event: ${result.error}")
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

    fun getEventsByUpcoming(finished: Int) {
        _upcomingEventData.value = Result.Loading
        viewModelScope.launch {
            eventRepository.getEventsByUpcoming(finished).observeForever { result ->
                _upcomingEventData.value = result
            }
        }
    }
}