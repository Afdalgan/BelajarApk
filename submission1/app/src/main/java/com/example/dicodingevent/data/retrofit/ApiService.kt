package com.example.dicodingevent.data.retrofit

import com.example.dicodingevent.data.response.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("events")
    suspend fun getEvent(@Query("active") active: String): Response

    @GET("events")
    fun getEvents(@Query("active") active: String): Response

    @GET("events/{id}")
    fun getDetailEvent(
        @Path("id") id: Int
    ): Call<DetailResponse>
}