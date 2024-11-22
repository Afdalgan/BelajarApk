package com.example.submission.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.submission.ApiConfig
import com.example.submission.Response.Response
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response as RetrofitResponse

class HomeViewModel : ViewModel() {
    private val _events = MutableLiveData<List<ListEventsItem?>?>()
    val events: LiveData<List<ListEventsItem?>?> get() = _events

    fun fetchEvents() {
        ApiConfig.getApiService().getEvents(0).enqueue(object : Callback<Response> {
            override fun onResponse(call: Call<Response>, response: RetrofitResponse<Response>) {
                if (response.isSuccessful) {
                    _events.postValue(response.body()?.listEvents)
                } else {
                    // Tangani error jika perlu
                    _events.postValue(emptyList()) // Atau tampilkan pesan error
                }
            }

            override fun onFailure(call: Call<Response>, t: Throwable) {
                // Tangani error
                _events.postValue(emptyList()) // Atau tampilkan pesan error
            }
        })
    }
}
