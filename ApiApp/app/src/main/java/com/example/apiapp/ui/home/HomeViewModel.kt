package com.example.apiapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ApiConfig
import android.content.ContentValues.TAG
import android.util.Log
import com.example.apiapp.data.Response.ListEventsItem
import com.example.apiapp.data.Response.Response
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response as RetrofitResponse

class HomeViewModel : ViewModel() {
    private val _events = MutableLiveData<List<ListEventsItem?>?>()
    val events: LiveData<List<ListEventsItem?>?> get() = _events

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    init {
        fetchEvents()
    }


    fun fetchEvents() {
        _isLoading.value = true
        val client =ApiConfig.getApiService().getEvents(0)
        client.enqueue(object : Callback<Response> {
            override fun onResponse(call: Call<Response>, response: RetrofitResponse<Response>) {
                _isLoading.value=false
                if (response.isSuccessful) {
                    _events.value= response.body()?.listEvents
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Response>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }
}
