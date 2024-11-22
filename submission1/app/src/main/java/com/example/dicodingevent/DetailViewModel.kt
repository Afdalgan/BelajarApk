package com.example.dicodingevent

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dicodingevent.data.EventRepository
import com.example.dicodingevent.data.entity.EventEntity
import kotlinx.coroutines.launch
import com.example.dicodingevent.data.Result

class DetailViewModel(private val eventRepository: EventRepository) : ViewModel() {

    private val _eventDetail: MutableLiveData<Result<EventEntity>> = MutableLiveData()
    val eventDetail: LiveData<Result<EventEntity>> get() = _eventDetail

    private val _errorMessage: MutableLiveData<String?> = MutableLiveData()
    val errorMessage: LiveData<String?> get() = _errorMessage

    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun toggleFavoriteStatus(event: EventEntity) {
        viewModelScope.launch {
            val newFavoriteStatus = !event.isFavorite
            eventRepository.setEventFavorite(event, newFavoriteStatus)

            val updatedEvent = event.copy(isFavorite = newFavoriteStatus)
            _eventDetail.value = Result.Success(updatedEvent)
        }
    }

    fun getEventDetailById(id: String) {
        _isLoading.value = true
        eventRepository.getDetailEvent(id).observeForever { result ->
            _isLoading.value = false
            _eventDetail.value = result
        }
    }




}