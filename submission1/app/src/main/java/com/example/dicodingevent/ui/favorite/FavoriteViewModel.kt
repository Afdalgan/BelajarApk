package com.example.dicodingevent.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dicodingevent.data.EventRepository
import com.example.dicodingevent.data.entity.EventEntity
import com.example.dicodingevent.data.Result


class FavoriteViewModel(private val eventRepository: EventRepository) : ViewModel() {

    private val _eventData: MutableLiveData<Result<List<EventEntity>>> = MutableLiveData()
    val eventData: LiveData<Result<List<EventEntity>>> get() = _eventData

    fun getFavoriteEvent() = eventRepository.getFavoriteEvent().observeForever { result ->
        _eventData.value = result
    }
}