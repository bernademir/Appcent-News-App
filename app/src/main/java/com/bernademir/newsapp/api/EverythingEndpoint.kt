package com.bernademir.newsapp.api

import io.reactivex.Observable
import com.bernademir.newsapp.model.Everything
import retrofit2.http.GET
import retrofit2.http.Query

interface EverythingEndpoint {

    @GET("top-headlines")
    fun getTopHeadlines(
        @Query("country") country: String,
        @Query("apiKey") apiKey: String
    ): Observable<Everything>

    @GET("everything")
    fun getUserSearchInput(
        @Query("apiKey") apiKey: String,
        @Query("q") q: String,
        @Query("page") page: Int
    ): Observable<Everything>
}