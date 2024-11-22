package com.dicoding.asclepius.response

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    companion object {
        const val API_KEY = "2040460560014eaa8e5692d98df47eae"
    }


    @GET("v2/top-headlines")
    fun getArticlesByQuery(
        @Query("category") category: String = "health",
        @Query("q") query: String ,
        @Query("language") language: String ,
        @Query("apiKey") apiKey: String
    ): Call<ArticleResponse>
}

